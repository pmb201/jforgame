package com.kingston.jforgame.server.game.collision;

/**
 * @Author puMengBin
 * @Date 2020-09-27 17:24
 * @Description
 */
public class CollisionDataPool {

    //cmd请求协议枚举
    /** 请求－创建房间 */
    public static final byte REQ_CREATE_ROOM = 1;
    /** 请求－加入房间 */
    public static final byte REQ_JOIN_ROOM = 2;
    /** 请求－离开房间 */
    public static final byte REQ_LEAVE_ROOM = 3;
    /** 请求－用户操作 */
    public static final byte REQ_USER_OPTION = 4;
    /** 请求－添加积分 */
    public static final byte REQ_ADD_SCORE = 5;

    //cmd响应协议枚举
    /** 响应－创建房间 */
    public static final byte RES_CREATE_ROOM = -1;
    /** 响应－加入房间 */
    public static final byte RES_JOIN_ROOM = -2;
    /** 响应－离开房间 */
    public static final byte RES_LEAVE_ROOM = -3;
    /** 响应－用户操作 */
    public static final byte RES_USER_OPTION = -4;
    /** 响应－添加积分 */
    public static final byte RES_ADD_SCORE = -5;
    /** 响应－用户操作帧同步 */
    public static final byte RES_USER_OPTIONS = -20;
}
