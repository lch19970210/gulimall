package com.hangzhou.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.gulimall.member.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:16:08
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

