package com.kingston.jforgame.server.game.collision.message.res;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;

import java.util.List;

/**
 * @Author puMengBin
 * @Date 2020-09-28 16:22
 * @Description
 */
@Data
@ProtobufClass
@MessageMeta(module = Modules.COLLISION, cmd = CollisionDataPool.RES_USER_OPTIONS)
public class ResUserOptions extends Message {

    private int frameSeq;

    private List<PlayerOptions> playerOptions;

}
