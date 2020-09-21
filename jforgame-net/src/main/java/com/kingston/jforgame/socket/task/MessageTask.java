package com.kingston.jforgame.socket.task;

import java.lang.reflect.Method;

import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingston.jforgame.socket.message.Message;

/**
 * when server receives a message, wrapped it into a MessageTask,
 * and add it to target message consumer task queue
 * @author kingston
 */
public class MessageTask extends AbstractDistributeTask {

	private static Logger logger = LoggerFactory.getLogger(MessageTask.class);

	/** owner playerId */
	private long playerId;
	/** io message content */
	private Message message;
	/** message controller */
	private Object handler;
	/** target method of the controller */
	private Method method;
	/**arguments passed to the method */
	private Object[] params;

	public static MessageTask valueOf(int distributeKey, Object handler,
			Method method, Object[] params) {
		MessageTask msgTask = new MessageTask();
		msgTask.distributeKey = distributeKey;
		msgTask.handler = handler;
		msgTask.method  = method;
		msgTask.params  = params;

		return msgTask;
	}

	@Override
	public void action() {
		try{
			Object response = method.invoke(handler, params);
			if (response != null) {
				IdSession session = SessionManager.INSTANCE.getSessionBy(playerId);
				session.sendPacket(message);
			}
		}catch(Exception e){
			logger.error("message task execute failed ", e);
		}
	}

	public long getPlayerId() {
		return playerId;
	}

	public Message getMessage() {
		return message;
	}

	public Object getHandler() {
		return handler;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getParams() {
		return params;
	}

	@Override
	public String toString() {
		return this.getName() + "[" + handler.getClass().getName() + "@" + method.getName() + "]";
	}

}
