package com.kingston.jforgame.server.game.person.message.req;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.person.PersonDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;

/**
 * @Author puMengBin
 * @Date 2020-09-07 19:15
 * @Description
 */
@MessageMeta(module= Modules.PERSON, cmd= PersonDataPool.REQ_TO_STRING)
public class ReqPersonToString extends Message {

    @Protobuf(order = 0)
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
