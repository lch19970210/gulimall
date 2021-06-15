package com.hangzhou.gulimall.member.exception;

/**
 * @Author linchenghui
 * @Date 2021/6/15
 */
public class UsernameExistException extends RuntimeException{
    public UsernameExistException() {
        super("用户名已存在");
    }
}
