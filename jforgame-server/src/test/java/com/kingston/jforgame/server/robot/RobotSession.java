package com.kingston.jforgame.server.robot;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.kingston.jforgame.server.game.login.message.res.ResAccountLogin;
import com.kingston.jforgame.server.game.player.message.ResCreateNewPlayer;
import com.kingston.jforgame.server.robot.handler.CreatePlayerHandler;
import com.kingston.jforgame.server.robot.handler.LoginSuccHandler;
import com.kingston.jforgame.socket.message.Message;

public class RobotSession {

	private Map<Class<? extends Message>, MessageHandler> handlers = new HashMap<>();

	private IoSession session;

	private Robot robot;

	public RobotSession(Robot robot, IoSession session) {
		this.robot = robot;
		this.session = session;
	}

	public void registerMessageHandler() {
		this.handlers.put(ResAccountLogin.class,
				new LoginSuccHandler());

		this.handlers.put(ResCreateNewPlayer.class,
				new CreatePlayerHandler());
	}

	public void receiveMessage(Message message) {
		Class<?> clazz = message.getClass();
		MessageHandler handler = handlers.get(clazz);
		if (handler != null) {
			handler.onMessageReceive(this, message);
		}
	}

	/**
	 * 发送消息
	 * @param message
	 */
	public void sendMessage(Message message) {
		this.session.write(message);
	}

	public Robot getPlayer() {
		return this.robot;
	}

}
