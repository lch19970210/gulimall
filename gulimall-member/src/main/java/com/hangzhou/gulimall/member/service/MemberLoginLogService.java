package com.hangzhou.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.gulimall.member.entity.MemberLoginLogEntity;

import java.util.Map;

/**
 * 会员登录记录
 *
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:16:08
 */
public interface MemberLoginLogService extends IService<MemberLoginLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

