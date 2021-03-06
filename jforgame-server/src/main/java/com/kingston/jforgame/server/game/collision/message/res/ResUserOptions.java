package com.kingston.jforgame.server.game.collision.message.res;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

import java.util.List;

/**
 * @Author puMengBin
 * @Date 2020-09-28 16:22
 * @Description
 */
@Data
@ProtobufClass
public class ResUserOptions {

    private int frameSeq;

    private List<PlayerOptions> playerOptions;

}
