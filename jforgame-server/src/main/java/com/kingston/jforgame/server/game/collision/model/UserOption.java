package com.kingston.jforgame.server.game.collision.model;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author puMengBin
 * @Date 2020-09-28 09:43
 * @Description
 */
@Data
@ProtobufClass
@AllArgsConstructor
@NoArgsConstructor
public class UserOption {

    private long accountId;

    private long dateTime;

    private byte[] optionData;
}
