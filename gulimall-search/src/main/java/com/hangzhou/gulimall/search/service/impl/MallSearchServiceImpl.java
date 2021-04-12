package com.hangzhou.gulimall.search.service.impl;

import com.hangzhou.gulimall.search.config.GulimallElasticSearchConfig;
import com.hangzhou.gulimall.search.constant.EsConstant;
import com.hangzhou.gulimall.search.service.MallSearchService;
import com.hangzhou.gulimall.search.vo.SearchParam;
import com.hangzhou.gulimall.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/4/8
 */
@Service
@Slf4j
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public SearchResult search(SearchParam param) {
        // 动态构建出查询需要的DSL语句
        SearchResult result = null;
        // 创建检索请求
        SearchRequest searchRequest = buildSearchRequest(param);

        try {
            // 执行检索请求
            SearchResponse response = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

            // 分析响应数据封装成需要的格式
            result = buildSearchResult(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 指定DSL检索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        return null;
    }

    /**
     * 创建检索请求
     * @return  检索请求
     */
    private SearchRequest buildSearchRequest(SearchParam param) {
        // 构建DSL语句
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 构建 bool query
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // 关键字模糊查询
        if(!StringUtils.isEmpty(param.getKeyword())){
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle",param.getKeyword()));
        }

        // 按照三级分类id查询
        if (param.getCatalog3Id()!=null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId",param.getCatalog3Id()));
        }

        // 按照品牌id查询
        if (param.getBrandId()!=null&&param.getBrandId().size()>0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId",param.getBrandId()));
        }

        // 按照是否有库存来进行查询
        if (param.getHasStock() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        }

        // 按照价格区间查询
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
        if(!StringUtils.isEmpty(param.getSkuPrice())){
            String[] prices = param.getSkuPrice().split("_");
            if (prices.length == 1){
                if (param.getSkuPrice().startsWith("_")){
                    rangeQueryBuilder.lte(Integer.parseInt(prices[0]));
                }else {
                    rangeQueryBuilder.gte(Integer.parseInt(prices[0]));
                }
            }else if(prices.length == 2 ){
                //_6000会截取成["","6000"]
                if(!prices[0].isEmpty()){
                    rangeQueryBuilder.gte(Integer.parseInt(prices[0]));
                }
                rangeQueryBuilder.lte(Integer.parseInt(prices[1]));
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }

        // 按照指定的商品属性查询
        // attrs=1_5寸:8寸&2_16G:8G
        List<String> attrs = param.getAttrs();
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        if(attrs != null && attrs.size() > 0){
            attrs.forEach(attr ->{
                String[] attrSplit = attr.split("_");
                queryBuilder.must(QueryBuilders.termQuery("attrs.attrId", attrSplit[0]));
                String[] attrValues = attrSplit[1].split(":");
                queryBuilder.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
            });
        }
        // 构造嵌入式查询条件,并且对评分不影响
        NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", queryBuilder, ScoreMode.None);
        boolQueryBuilder.filter(nestedQueryBuilder);

        // bool query构建完成
        searchSourceBuilder.query(boolQueryBuilder);

        // 排序
        // 参数字段_ASC(DESC)
        if (!StringUtils.isEmpty(param.getSort())){
            String[] sortSplit = param.getSort().split("_");
            searchSourceBuilder.sort(sortSplit[0],sortSplit[1].equalsIgnoreCase("asc")? SortOrder.ASC:SortOrder.DESC);
        }

        // 分页
        searchSourceBuilder.from((param.getPageNum()-1) * EsConstant.PRODUCT_PAGESIZE);
        searchSourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);

        // 字段高亮
        if(!StringUtils.isEmpty(param.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTile");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }

        // 聚合分析
        // 按照品牌聚合
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brandAgg").field("brandId");
        TermsAggregationBuilder brandNameAgg = AggregationBuilders.terms("brandNameAgg").field("brandName");
        TermsAggregationBuilder brandImgAgg = AggregationBuilders.terms("brandImgAgg").field("brandImg");
        brandAgg.subAggregation(brandNameAgg);
        brandAgg.subAggregation(brandImgAgg);
        searchSourceBuilder.aggregation(brandAgg);

        // 按照分类聚合
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalogAgg").field("catalogId");
        // 子聚合
        TermsAggregationBuilder catalogNameAgg = AggregationBuilders.terms("catalogNameAgg").field("catalogName");
        catalogAgg.subAggregation(catalogNameAgg);
        searchSourceBuilder.aggregation(catalogAgg);

        // 按照属性(嵌入式字段)聚合
        NestedAggregationBuilder nestedAggregationBuilder = new NestedAggregationBuilder("attrs", "attrs");
        //按照attrId聚合     //按照attrId聚合之后再按照attrName和attrValue聚合
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId");
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrNameAgg").field("attrs.attrName");
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue");
        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);

        nestedAggregationBuilder.subAggregation(attrIdAgg);
        searchSourceBuilder.aggregation(nestedAggregationBuilder);

        log.debug("构建的DSL语句 {}",searchSourceBuilder.toString());
        System.out.println("构建的DSL语句："+searchSourceBuilder.toString());

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, searchSourceBuilder);
        return searchRequest;

    }

    /**
     * 分析响应数据封装成需要的格式
     * @param response  响应数据
     * @return  相对于的格式
     */
    private SearchResult buildSearchResult(SearchResponse response) {
        SearchResult result = new SearchResult();
        return result;
    }
}
