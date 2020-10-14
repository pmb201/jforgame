package com.kingston.jforgame.server.game.collision.message.req;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;

/**
 * @Author puMengBin
 * @Date 2020-09-28 10:59
 * @Description
 */
@Data
@ProtobufClass
@MessageMeta(module = Modules.COLLISION,cmd = CollisionDataPool.REQ_ADD_SCORE)
public class ReqAddScore extends Message {

    private int index;

    private long accountId;

    private int score;
}
