package com.hangzhou.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.gulimall.order.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:23:46
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

