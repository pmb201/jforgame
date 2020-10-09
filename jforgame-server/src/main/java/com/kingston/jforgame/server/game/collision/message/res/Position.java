package com.kingston.jforgame.server.game.collision.message.res;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author puMengBin
 * @Date 2020-09-28 20:37
 * @Description
 */
@Data
@ProtobufClass
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    private float x;

    private float y;

    private float z;

}
