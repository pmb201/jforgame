package com.kingston.jforgame.server.game.person.message;

import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.person.PersonDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;

/**
 * @Author puMengBin
 * @Date 2020-09-07 19:16
 * @Description
 */
@MessageMeta(module= Modules.PERSON, cmd= PersonDataPool.RES_TO_STRING)
public class ResPersonToString extends Message {
}
