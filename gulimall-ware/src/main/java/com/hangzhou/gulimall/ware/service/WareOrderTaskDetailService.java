package com.hangzhou.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.gulimall.ware.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:29:23
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

