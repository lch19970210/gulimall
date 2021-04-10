package com.hangzhou.gulimall.search.service;

import com.hangzhou.gulimall.search.vo.SearchParam;
import com.hangzhou.gulimall.search.vo.SearchResult;

/**
 * @Author linchenghui
 * @Date 2021/4/8
 */
public interface MallSearchService {
    /**
     *
     * @param param 检索参数
     * @return  检索结果
     */
    SearchResult search(SearchParam param);
}
