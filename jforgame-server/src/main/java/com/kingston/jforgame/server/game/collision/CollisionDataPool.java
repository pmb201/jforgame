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
    /** 请求－开始游戏 */
    public static final byte REQ_START_GAME = 6;
    /** 请求－开始游戏 */
    public static final byte REQ_GAME_READY = 7;
    /** 请求－用户位置同步 */
    public static final byte REQ_USER_POSITION = 21;
    /** 请求－游戏用户碰撞 */
    public static final byte REQ_USER_COLLISION = 24;
    /** 请求－游戏结束 */
    public static final byte REQ_GAME_END = 25;
    /** 请求－游戏用户移动 */
    public static final byte REQ_USER_TO_MOVE = 26;

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
    /** 响应－开始游戏 */
    public static final byte RES_START_GAME = -6;
    /** 响应－用户操作帧同步 */
    public static final byte RES_USER_OPTIONS = -20;
    /** 响应－用户位置同步初始广播 */
    public static final byte RES_USER_POSITIONS = -22;
    /** 响应－用户位置同步 */
    public static final byte RES_USER_POSITION = -21;
    /** 响应－游戏房间人数已满 */
    public static final byte RES_USER_CAN_READY = -23;
    /** 响应－游戏用户碰撞 */
    public static final byte RES_USER_COLLISION = -24;
    /** 响应－游戏结束推送 */
    public static final byte RES_GAME_END = -25;
    /** 响应－游戏用户移动 */
    public static final byte RES_USER_TO_MOVE = -26;
}
