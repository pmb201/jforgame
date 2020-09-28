package com.kingston.jforgame.server.game.collision.message.req;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;

/**
 * @Author puMengBin
 * @Date 2020-09-27 17:22
 * @Description
 */
@MessageMeta(module = Modules.COLLISION,cmd = CollisionDataPool.REQ_JOIN_ROOM)
@ProtobufClass
@Data
public class ReqJoinRoom extends Message {

    @Protobuf
    private long roomId;
}
