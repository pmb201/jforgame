package com.kingston.jforgame.server.game.collision.message.res;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;

/**
 * @Author puMengBin
 * @Date 2020-10-12 14:19
 * @Description
 */
@Data
@ProtobufClass(description = "module = 109, cmd = -24, 用户碰撞广播")
@MessageMeta(module = Modules.COLLISION, cmd = CollisionDataPool.RES_USER_COLLISION)
public class ResCollision extends Message {

    /*@Protobuf(description = "碰撞对象双方")
    public List<Collision> collisions;*/

    @Protobuf(description = "玩家下标")
    private int index;

    @Protobuf(description = "玩家账号")
    private long accountId;

    @Protobuf(description = "速度")
    private double speed;

    @Protobuf(description = "衰减速度")
    private double subSpeed;

    @Protobuf(description = "欧拉角")
    private double angle;


}
