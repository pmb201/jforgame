package com.kingston.jforgame.server.game.room.model;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.kingston.jforgame.common.utils.ConcurrentHashSet;
import com.kingston.jforgame.server.db.BaseEntity;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.game.collision.CollisionManager;
import com.kingston.jforgame.server.game.collision.model.UserOption;
import com.kingston.jforgame.server.redis.RoomIdBuilder;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author puMengBin
 * @Date 2020-09-24 10:54
 * @Description
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name="room_profile")
public class RoomProfile extends BaseEntity {

    @Protobuf(order = 1)
    @Column
    @Id
    private long id;

    @Protobuf(order = 2)
    @Column
    private String name;

    @Column
    private int maxPlayerNum;

    private AtomicInteger playerNum = new AtomicInteger(0);

    private AtomicInteger frameSeq = new AtomicInteger(1);

    /** 玩家列表 **/
    @Protobuf(fieldType = FieldType.OBJECT,order = 3)
    @Column
    private ConcurrentHashSet<AccountProfile> accounts;

    /** 房间状态: 0,创建；1,已开始； **/
    @Protobuf(order = 4)
    @Column
    private int roomStatus;

    /** 创建时间 **/
    @Protobuf(order = 5)
    @Column
    private long createTime;

    /** 开局时间（游戏开始） **/
    @Column
    private long startTime;

    /** 是否添加定时关闭房间 **/
    @Column
    private boolean timer;

    /** 房间所属游戏信息 **/
    @Protobuf(order = 6)
    @Column
    private String appId;

    /** 房间所在游戏服地址 **/
    @Protobuf(order = 7)
    @Column
    private String gameServer;

    /** 全局用户操作记录 **/
    private List<UserOption> userOptionList;

    public RoomProfile(String appId,AccountProfile account){
        this.id = RoomIdBuilder.buildRoomId();
        this.name = String.valueOf(id);
        this.accounts = new ConcurrentHashSet<>();
        accounts.add(account);
        this.createTime = System.currentTimeMillis();
        this.roomStatus = Status.CREATE.getCode();
        this.timer = false;
        this.appId = appId;
        if(appId.equals(CollisionManager.appId)){
            this.maxPlayerNum = 9;
        }
    }

    public boolean isOpen(){
        return this.roomStatus == Status.CREATE.getCode();
    }

    public boolean canJoin(){
        return this.roomStatus == Status.CREATE.getCode() && playerNum.intValue() < maxPlayerNum;
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
