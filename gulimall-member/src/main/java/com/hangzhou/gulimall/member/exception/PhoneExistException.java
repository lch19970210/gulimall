package com.hangzhou.gulimall.member.exception;

/**
 * @Author linchenghui
 * @Date 2021/6/15
 */
public class PhoneExistException extends RuntimeException{
    public PhoneExistException() {
        super("手机号存在");
    }
}
