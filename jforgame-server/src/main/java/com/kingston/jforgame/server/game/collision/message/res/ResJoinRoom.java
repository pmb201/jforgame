package com.kingston.jforgame.server.game.collision.message.res;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.server.game.room.model.RoomProfile;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;

/**
 * @Author puMengBin
 * @Date 2020-09-27 17:37
 * @Description
 */
@MessageMeta(module = Modules.COLLISION,cmd = CollisionDataPool.RES_JOIN_ROOM)
@ProtobufClass
@Data
public class ResJoinRoom extends Message {

    private RoomProfile roomProfile;
}
