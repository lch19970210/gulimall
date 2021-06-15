package com.hangzhou.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.gulimall.member.entity.MemberEntity;
import com.hangzhou.gulimall.member.exception.PhoneExistException;
import com.hangzhou.gulimall.member.exception.UsernameExistException;
import com.hangzhou.gulimall.member.vo.MemberRegistVo;

import java.util.Map;

/**
 * 会员
 *
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:16:08
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberRegistVo vo);

    void checkMobileUnique(String mobile) throws PhoneExistException;

    void checkUsernameUnique(String mobile) throws UsernameExistException;
}

