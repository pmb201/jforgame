package com.kingston.jforgame.server.game.collision.message.res;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;

/**
 * @Author puMengBin
 * @Date 2020-09-28 09:29
 * @Description
 */
@MessageMeta(module = Modules.COLLISION,cmd = CollisionDataPool.RES_USER_OPTION)
@Data
@ProtobufClass
public class ResUserOption extends Message {


}
