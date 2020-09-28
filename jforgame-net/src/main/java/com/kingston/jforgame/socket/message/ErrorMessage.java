package com.kingston.jforgame.socket.message;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import lombok.ToString;

/**
 * @Author puMengBin
 * @Date 2020-09-26 17:07
 * @Description
 */
@MessageMeta(module=ErrorMessageMeta.ERROR_MODULES, cmd=ErrorMessageMeta.ERROR_CMD)
@ProtobufClass
@ToString
public class ErrorMessage extends Message{

    public ErrorMessage(){}

    public ErrorMessage(int code,String message){
        this.code = code;
        this.message = message;
    }

    private int code;

    private String message;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
