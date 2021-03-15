package com.hangzhou.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author linchenghui
 * @Date 2021/3/15
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Catelog2Vo {
    private String catalogId;
    private List<Catelog3Vo> catalog3List;
    private String id;
    private String name;

    /**
     * 三级分类的静态内部类
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Catelog3Vo{
        private String catalog2Id;
        private String id;
        private String name;
    }
}
