package com.kingston.jforgame.server.game.collision.message.req;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.server.game.collision.message.res.Position;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;

/**
 * @Author puMengBin
 * @Date 2020-10-13 14:52
 * @Description
 */
@Data
@ProtobufClass
@MessageMeta(module = Modules.COLLISION, cmd = CollisionDataPool.REQ_USER_TO_MOVE)
public class ReqUserTMove extends Message {

    @Protobuf(description = "速度")
    private double speed;

    @Protobuf(description = "衰减速度")
    private double subSpeed;

    @Protobuf(description = "碰撞点")
    private Position hitPos;

    @Protobuf(description = "欧拉角")
    private double angle;

}
