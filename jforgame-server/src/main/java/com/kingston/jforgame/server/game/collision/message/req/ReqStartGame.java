package com.kingston.jforgame.server.game.collision.message.req;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;

/**
 * @Author puMengBin
 * @Date 2020-09-28 19:51
 * @Description
 */
@Data
@ProtobufClass
@MessageMeta(module = Modules.COLLISION, cmd = CollisionDataPool.REQ_START_GAME)
public class ReqStartGame extends Message {
}
