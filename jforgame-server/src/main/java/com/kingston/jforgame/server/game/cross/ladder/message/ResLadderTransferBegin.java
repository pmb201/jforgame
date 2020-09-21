package com.kingston.jforgame.server.game.cross.ladder.message;

import com.kingston.jforgame.server.game.Modules;
import com.kingston.jforgame.server.game.cross.ladder.service.LadderDataPool;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;

/**
 * 通知客户端切换socket到战斗服
 *
 */
@MessageMeta(module = Modules.LADDER, cmd = LadderDataPool.RES_LADDER_TRANSFER_BEGIN)
public class ResLadderTransferBegin extends Message {
	
	/**
	 * 传输密钥（战斗服登录密码）
	 */
	private String sign;

	/**
	 * 战斗服ip地址
	 */
	private String targetIp;
	
	/**
	 * 战斗服端口
	 */
	private int targetPort;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTargetIp() {
		return targetIp;
	}

	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}

	public int getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}
	
}
