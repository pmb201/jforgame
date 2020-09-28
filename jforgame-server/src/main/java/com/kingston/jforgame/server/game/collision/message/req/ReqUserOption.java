package com.kingston.jforgame.server.game.collision.message.req;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.collision.CollisionDataPool;
import com.kingston.jforgame.server.game.collision.model.UserOption;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;

/**
 * @Author puMengBin
 * @Date 2020-09-28 09:23
 * @Description
 */
@MessageMeta(module = Modules.COLLISION,cmd = CollisionDataPool.REQ_USER_OPTION)
@Data
@ProtobufClass
public class ReqUserOption extends Message {

    private byte[] optionData;

    public UserOption build(long accountId){
        UserOption option = new UserOption();
        option.setAccountId(accountId);
        option.setOptionData(optionData);
        return option;
    }

}
