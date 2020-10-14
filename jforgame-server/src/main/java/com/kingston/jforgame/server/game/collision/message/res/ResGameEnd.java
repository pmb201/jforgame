package com.kingston.jforgame.server.game.collision.message.res;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;

import java.util.List;

/**
 * @Author puMengBin
 * @Date 2020-10-12 15:16
 * @Description
 */
@Data
@ProtobufClass
@MessageMeta(module = Modules.COLLISION,cmd = CollisionDataPool.RES_GAME_END)
public class ResGameEnd extends Message {

    @Protobuf(description = "当前游戏分数排名")
    private List<AccountScore> accountScoreList;
}
