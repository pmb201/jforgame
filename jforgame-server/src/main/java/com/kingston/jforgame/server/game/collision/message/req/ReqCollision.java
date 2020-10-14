package com.kingston.jforgame.server.game.collision.message.req;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;
import lombok.ToString;

/**
 * @Author puMengBin
 * @Date 2020-10-12 13:56
 * @Description
 */
@Data
@ProtobufClass
@MessageMeta(module = Modules.COLLISION,cmd = CollisionDataPool.REQ_USER_COLLISION)
@ToString
public class ReqCollision extends Message {

    /*@Protobuf(description = "碰撞对象列表")
    public List<Collision> collisions;*/

    @Protobuf(description = "速度")
    private double speed;

    @Protobuf(description = "衰减速度")
    private double subSpeed;

    @Protobuf(description = "欧拉角")
    private double angle;

    @Protobuf(description = "被碰撞玩家id")
    private long accountId;

}
