package com.kingston.jforgame.server.game.collision.message.res;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @Author puMengBin
 * @Date 2020-10-13 15:45
 * @Description
 */
@Data
@ProtobufClass
public class Collision {

    @Protobuf(description = "玩家下标")
    private int index;

    @Protobuf(description = "玩家账号")
    private long accountId;

    @Protobuf(description = "速度")
    private double speed;

    @Protobuf(description = "衰减速度")
    private double subSpeed;

    @Protobuf(description = "碰撞点")
    private Position hitPos;

    @Protobuf(description = "欧拉角")
    private double angle;
}
