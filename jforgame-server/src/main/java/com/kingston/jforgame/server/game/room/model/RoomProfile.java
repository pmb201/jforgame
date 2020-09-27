package com.kingston.jforgame.server.game.room.model;

import com.kingston.jforgame.server.game.accout.entity.Account;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.redis.RoomIdBuilder;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author puMengBin
 * @Date 2020-09-24 10:54
 * @Description
 */
@Data
@ToString
public class RoomProfile {

    private long id;

    private String name;

    private int maxPlayerNum;

    private AtomicInteger playerNum = new AtomicInteger(0);

    /** 玩家列表 **/
    private Set<AccountProfile> accounts;

    /** 房间状态: 0,创建；1,已开始； **/
    private int status;

    /** 创建时间 **/
    private long createTime;

    /** 开局时间（游戏开始） **/
    private long startTime;

    /** 是否添加定时关闭房间 **/
    private boolean timer;

    /** 房间所属游戏信息 **/
    private String appId;

    /** 房间所在游戏服地址 **/
    private String gameServer;

    public RoomProfile(String appId,AccountProfile account){
        this.id = RoomIdBuilder.buildRoomId();
        this.name = String.valueOf(id);
        this.accounts = new HashSet<>();
        accounts.add(account);
        this.createTime = System.currentTimeMillis();
        this.status = Status.CREATE.getCode();
        this.timer = false;
        this.appId = appId;
    }

    public static enum  Status {
        CREATE(0),CLOSE(1);

        private Status(int code){
            this.code = code;
        }

        public static Status valueOf(int code) {
            switch (code) {
                case 0:
                    return CREATE;
                case 1:
                    return CLOSE;
            }
            return null;
        }

        private int code;

        public int getCode() {
            return code;
        }
    }
}
