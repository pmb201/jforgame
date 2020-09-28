package com.kingston.jforgame.server.game.collision.message.res;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.server.game.room.model.RoomProfile;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author puMengBin
 * @Date 2020-09-27 17:36
 * @Description
 */
@MessageMeta(module = Modules.COLLISION,cmd = CollisionDataPool.RES_CREATE_ROOM)
@ProtobufClass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCreateRoom extends Message {

    private RoomProfile roomProfile;
}
