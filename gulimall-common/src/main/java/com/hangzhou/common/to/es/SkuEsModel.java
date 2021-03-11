package com.hangzhou.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/3/9
 */
@Data
public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;

    private List<Attrs> attrs;

    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
    /**
     *             "skuId":{ "type": "long" },
     *             "spuId":{ "type": "keyword" },  # 不可分词
     *             "skuTitle": {
     *                 "type": "text",
     *                 "analyzer": "ik_smart"  # 中文分词器
     *             },
     *             "skuPrice": { "type": "keyword" },
     *             "skuImg"  : { "type": "keyword" },
     *             "saleCount":{ "type":"long" },
     *             "hasStock": { "type": "boolean" },
     *             "hotScore": { "type": "long"  },
     *             "brandId":  { "type": "long" },
     *             "catalogId": { "type": "long"  },
     *             "brandName": {"type": "keyword"},
     *             "brandImg":{
     *                 "type": "keyword",
     *                 "index": false,  # 不可被检索，不生成index
     *                 "doc_values": false # 不可被聚合
     *             },
     *             "catalogName": {"type": "keyword" },
     *             "attrs": {
     *                 "type": "nested",
     *                 "properties": {
     *                     "attrId": {"type": "long"  },
     *                     "attrName": {
     *                         "type": "keyword",
     *                         "index": false,
     *                         "doc_values": false
     *                     },
     *                     "attrValue": {"type": "keyword" }
     *                 }
     *             }
     */
}
