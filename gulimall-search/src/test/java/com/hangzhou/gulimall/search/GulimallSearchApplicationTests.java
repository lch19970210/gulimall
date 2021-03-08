package com.hangzhou.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.hangzhou.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class GulimallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Test
    void contextLoads() {
        System.out.println(client.toString());
    }

    @Test
    public void searchData() throws IOException {
        // 创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        // 指定索引
        searchRequest.indices("bank");
        // 指定DSL检索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("address","mill"));

        // 构建复杂检索条件
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");

        sourceBuilder.aggregation(ageAgg);
        sourceBuilder.aggregation(balanceAvg);

        System.out.println("检索条件"+sourceBuilder.toString());
        searchRequest.source(sourceBuilder);

        // 执行检索
        SearchResponse response = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        // 执行结果
        System.out.println(response.toString());
        // 封装检索结果
        SearchHits hits = response.getHits();
        for (SearchHit hit:hits) {
            String sourceAsString = hit.getSourceAsString();
            System.out.println("sourceAsString:"+sourceAsString);
        }

        // 获取检索到的分析信息
        Aggregations aggregations = response.getAggregations();
        Terms aggregation = aggregations.get("ageAgg");
        for(Terms.Bucket bucket: aggregation.getBuckets()){
            String string = bucket.getKeyAsString();
            System.out.println("bucket.getKeyAsString():"+bucket.getKeyAsString());
        }

    }

    @Test
    public void indexData() throws IOException {
        User user = new User();
        user.setUserName("ccc");
        user.setGender("女");
        user.setAge(18);
        // 构建请求
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);
        IndexResponse index = client.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }

}

@Data
class User{
    private String userName;
    private String gender;
    private Integer age;
}
