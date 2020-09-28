package com.kingston.jforgame.server.game.collision.message.res;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.List;

/**
 * @Author puMengBin
 * @Date 2020-09-28 17:10
 * @Description
 */
@ProtobufClass
@Data
public class PlayerOptions {

    private int playerIndex;

    private List<byte[]> options;
}
