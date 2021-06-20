package com.hangzhou.gulimall.member.service.impl;

import com.hangzhou.gulimall.member.dao.MemberLevelDao;
import com.hangzhou.gulimall.member.exception.PhoneExistException;
import com.hangzhou.gulimall.member.exception.UsernameExistException;
import com.hangzhou.gulimall.member.vo.MemberLoggingVo;
import com.hangzhou.gulimall.member.vo.MemberRegistVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.common.utils.Query;

import com.hangzhou.gulimall.member.dao.MemberDao;
import com.hangzhou.gulimall.member.entity.MemberEntity;
import com.hangzhou.gulimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Autowired
    MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void regist(MemberRegistVo vo) {
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = new MemberEntity();
        // 设置默认等级
        MemberEntity levelEntity = memberLevelDao.getDefaultLevel();
        entity.setLevelId(levelEntity.getId());
        // 检查用户名和手机号是否唯一
        checkMobileUnique(vo.getPhone());
        checkUsernameUnique(vo.getUserName());
        entity.setMobile(vo.getPhone());
        entity.setUsername(vo.getUserName());
        // 密码进行加密处理
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 虽然每次加盐处理得出的值不一致但是用 passwordEncoder.matches() 方法还是可以进行比较的
        String encode = passwordEncoder.encode(vo.getPassword());
        entity.setPassword(encode);
        // todo 其他默认值设置
        memberDao.insert(entity);
    }

    @Override
    public MemberEntity login(MemberLoggingVo vo) {
        String userName = vo.getLoginacct();
        String password = vo.getPassword();
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", userName)
                .or().eq("password", password));
        if (ObjectUtils.isEmpty(entity)) {
            return null;
        } else {
            String passwordFromDB = entity.getPassword();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean matches = encoder.matches(password, passwordFromDB);
            if (matches) {
                return entity;
            } else {
                return null;
            }
        }
    }

    @Override
    public void checkMobileUnique(String mobile) {
        Integer integer = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", mobile));
        if (integer > 0) {
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUsernameUnique(String username) {
        Integer integer = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (integer > 0) {
            throw new UsernameExistException();
        }
    }

}

