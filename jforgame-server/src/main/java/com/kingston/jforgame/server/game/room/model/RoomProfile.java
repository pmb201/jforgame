package com.kingston.jforgame.server.game.room.model;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.kingston.jforgame.common.utils.BlockingUniqueQueue;
import com.kingston.jforgame.common.utils.DateUtil;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.game.collision.message.res.ResPlayersPosition;
import com.kingston.jforgame.server.game.play.record.entity.PlayRecord;
import com.kingston.jforgame.server.redis.RoomIdBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author puMengBin
 * @Date 2020-09-24 10:54
 * @Description
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class RoomProfile {

    @Protobuf(order = 1)
    private long id;

    @Protobuf(order = 2)
    private String name;

    private int maxPlayerNum;

    private AtomicInteger playerNum = new AtomicInteger(0);

    private AtomicInteger frameSeq = new AtomicInteger(1);

    /** 玩家列表 **/
    /*@Protobuf(fieldType = FieldType.OBJECT,order = 3)
    private ConcurrentHashSet<AccountProfile> accounts;*/

    private BlockingUniqueQueue<ResPlayersPosition> userOptions = new BlockingUniqueQueue<>();

    private AccountProfile[] accountProfiles;

    /** 房间状态: 0,创建；1,已开始； **/
    @Protobuf(order = 4)
    private int roomStatus;

    /** 创建时间 **/
    @Protobuf(order = 5)
    private long createTime;

    /** 开局时间（游戏开始） **/
    private long startTime;

    /** 是否添加定时关闭房间 **/
    private boolean timer;

    /** 房间所属游戏信息 **/
    @Protobuf(order = 6)
    private String appId;

    /** 房间所在游戏服地址 **/
    @Protobuf(order = 7)
    private String gameServer;

    private int index;

    /** 全局用户操作记录 **/
    private List<ResPlayersPosition> userOptionList;

    public RoomProfile(String appId,int maxPlayerNum,AccountProfile account){
        this.id = RoomIdBuilder.buildRoomId();
        this.name = String.valueOf(id);
        //this.accounts = new ConcurrentHashSet<>();
        //accounts.add(account);
        this.createTime = System.currentTimeMillis();
        this.roomStatus = Status.CREATE.getCode();
        this.timer = false;
        this.appId = appId;
        if(maxPlayerNum > 1){
            this.maxPlayerNum = maxPlayerNum;
        }else{
            this.maxPlayerNum = 5;
        }
        accountProfiles = new AccountProfile[maxPlayerNum];
        accountProfiles[0] = account;
        this.playerNum.incrementAndGet();
    }

    public boolean isOpen(){
        return this.roomStatus == Status.CREATE.getCode();
    }

    public boolean canJoin(){
        return this.roomStatus == Status.CREATE.getCode() && playerNum.intValue() < maxPlayerNum;
    }

    public boolean isEmptyRoom(){
        if(this == null){
            return true;
        }
        AccountProfile[] accountProfiles = getAccountProfiles();
        if(accountProfiles == null){
            return true;
        }
        for(AccountProfile accountProfile : accountProfiles){
            if(accountProfile != null){
                return false;
            }
        }
        return true;
    }

    public boolean isFull(){
        if(this == null){
            return false;
        }
        AccountProfile[] accountProfiles = getAccountProfiles();
        if(accountProfiles == null){
            return false;
        }
        for(AccountProfile accountProfile : accountProfiles){
            if(accountProfile == null){
                return false;
            }
        }
        return true;
    }

    public boolean isAllReady(){
        if(this == null){
            return false;
        }
        AccountProfile[] accountProfiles = getAccountProfiles();
        if(accountProfiles == null){
            return false;
        }
        for(AccountProfile accountProfile : accountProfiles){
            if(accountProfile == null){
                return false;
            }
            if(accountProfile.getStatus() != AccountProfile.Status.GAMING.getCode()){
                return false;
            }
        }
        return true;
    }

    public PlayRecord buildPlayRecord(){
        PlayRecord record = new PlayRecord();
        record.setId(System.currentTimeMillis());
        record.setAppId(this.appId);
        record.setCreateTime(DateUtil.format(new Date()));
        String[] accountIds = Arrays.stream(accountProfiles)
                .filter(accountProfile -> accountProfile != null)
                .sorted(Comparator.comparing(AccountProfile::getIntScore).reversed())
                .map(accountProfile -> accountProfile.getId().toString())
                .toArray(String[] :: new);
        record.setAccounts(String.join(",",accountIds));
        return record;
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
