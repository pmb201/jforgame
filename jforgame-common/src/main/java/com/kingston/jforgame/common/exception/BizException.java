package com.kingston.jforgame.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author puMengBin
 * @Date 2020-09-26 14:43
 * @Description
 */
public class BizException extends RuntimeException{

    @Getter
    private IErrorCode errorCode;
    @Setter
    @Getter
    private Object data;

    public BizException(IErrorCode errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public BizException(IErrorCode errorCode, Exception e) {
        super(e);
        this.errorCode = errorCode;
    }

    public BizException(IErrorCode errorCode, Exception e, String message) {
        super(message, e);
        this.errorCode = errorCode;
    }

    public BizException(IErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }
}
