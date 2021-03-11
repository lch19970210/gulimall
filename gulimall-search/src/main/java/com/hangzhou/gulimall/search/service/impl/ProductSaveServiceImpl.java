package com.hangzhou.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.hangzhou.common.to.es.SkuEsModel;
import com.hangzhou.gulimall.search.config.GulimallElasticSearchConfig;
import com.hangzhou.gulimall.search.constant.EsConstant;
import com.hangzhou.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author linchenghui
 * @Date 2021/3/11
 */
@Service
@Slf4j
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        // 在es建立索引,建立好映射关系

        // 在es保存数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel model : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());
            String s = JSON.toJSONString(model);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        // 处理异常信息
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.info("商品上架成功:{}",collect);

        return b;


    }
}
