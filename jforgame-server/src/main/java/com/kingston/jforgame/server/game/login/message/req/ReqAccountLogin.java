package com.kingston.jforgame.server.game.login.message.req;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.login.LoginDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;
import lombok.Data;

/**
 * 请求－账号登录
 * @author kingston
 */
@Data
@MessageMeta(module=Modules.LOGIN, cmd=LoginDataPool.REQ_LOGIN)
public class ReqAccountLogin extends Message {
	
	/** 账号流水号 */
	@Protobuf(order = 1)
	private long accountId;
	
	@Protobuf(order = 2)
	private String password;

	@Protobuf(order = 3,required = true)
	private String unionId;

	@Protobuf(order = 4)
	private String appId;

	@Protobuf(order = 5)
	private String code;

	@Override
	public String toString() {
		return "ReqLoginMessage [accountId=" + accountId + ", password="
				+ password + "]";
	}

}
