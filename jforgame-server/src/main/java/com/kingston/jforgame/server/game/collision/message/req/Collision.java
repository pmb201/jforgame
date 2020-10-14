package com.kingston.jforgame.server.game.collision.message.req;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.collision.message.res.Position;
import lombok.Data;

/**
 * @Author puMengBin
 * @Date 2020-10-13 15:42
 * @Description
 */
@Data
@ProtobufClass
public class Collision {
    @Protobuf(description = "速度")
    private double speed;

    @Protobuf(description = "衰减速度")
    private double subSpeed;

    @Protobuf(description = "碰撞点")
    private Position hitPos;

    @Protobuf(description = "欧拉角")
    private double angle;

    @Protobuf(description = "被碰撞玩家id")
    private long accountId;
}
