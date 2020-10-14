package com.kingston.jforgame.server.game.collision.message.res;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;

/**
 * @Author puMengBin
 * @Date 2020-10-12 09:14
 * @Description
 */
@Data
@ProtobufClass
public class AccountScore {

    private int index;

    private long accountId;

    private int score;

}
