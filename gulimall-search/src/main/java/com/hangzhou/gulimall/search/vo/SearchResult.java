package com.hangzhou.gulimall.search.vo;

import com.hangzhou.common.to.es.SkuEsModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/4/8
 */
@Data
@Accessors(chain = true)
public class SearchResult {
    /**
     * 查询到的所有商品信息
     */
    private List<SkuEsModel> products;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 总页码
     */
    private Integer totalPages;

    /**
     * 所有涉及到的品牌
     */
    private List<BrandVo> brands;
    /**
     * 所有涉及到的分类
     */
    private List<CatalogVo> catalogs;
    /**
     * 所有涉及到所有属性
     */
    private List<AttrVo> attrs;

    /**
     * 导航页,页码遍历结果集(分页)
     */
    private List<Integer> pageNavs;

    /**
     * 导航数据
     */
    private List<NavVo> navs = new ArrayList<>();

    /**
     * 便于判断当前id是否被使用
     */
    private List<Long> attrIds = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    public static class NavVo {
        private String name;
        private String navValue;
        private String link;
    }

    @Data
    @Accessors(chain = true)
    public static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    @Accessors(chain = true)
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }

    @Data
    @Accessors(chain = true)
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
