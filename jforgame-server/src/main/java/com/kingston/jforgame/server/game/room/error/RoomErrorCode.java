package com.kingston.jforgame.server.game.room.error;

import com.kingston.jforgame.common.exception.IErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * @Author puMengBin
 * @Date 2020-09-26 14:54
 * @Description
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RoomErrorCode implements IErrorCode {

    ACCOUNT_NOT_EXIST(4001, "房间创建失败，用户未授权或登陆。"),
    ROOM_NOT_EXIST(4002, "房间不存在。"),
    ROOM_CAN_NOT_JOIN(4003, "不能加入已经开始游戏的房间。"),
    ;

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
