package com.kingston.jforgame.server.game.collision.error;

import com.kingston.jforgame.common.exception.IErrorCode;
import lombok.AllArgsConstructor;

/**
 * @Author puMengBin
 * @Date 2020-09-27 17:50
 * @Description
 */
@AllArgsConstructor
public enum  CollisionError implements IErrorCode {
    USER_NOT_LOGIN(4101,"用户未登陆，不能创建游戏房间"),
    USER_NOT_IN_GAME(4102,"用户没有在游戏中，不能上报操作数据");


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
