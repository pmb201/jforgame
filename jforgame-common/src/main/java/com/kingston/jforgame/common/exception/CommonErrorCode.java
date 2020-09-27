package com.kingston.jforgame.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * @Author puMengBin
 * @Date 2020-09-26 14:44
 * @Description
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommonErrorCode implements IErrorCode {

    PARAM_EXCEPTION(1000, "参数验证失败"),
    COMMON_BIZ_EXCEPTION(2000, "业务异常"),
    UNLOGIN_ERROR(3000, "未登录");

    private int code;
    private String message;

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

}
