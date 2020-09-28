package com.kingston.jforgame.socket.task;

import com.kingston.jforgame.common.exception.BizException;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.message.ErrorMessage;
import com.kingston.jforgame.socket.message.Message;
import com.kingston.jforgame.socket.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * when server receives a message, wrapped it into a MessageTask,
 * and add it to target message consumer task queue
 * @author kingston
 */
public class MessageTask extends AbstractDistributeTask {

	private static Logger logger = LoggerFactory.getLogger(MessageTask.class);

	/** owner playerId */
	private long accountId;
	/** io message content */
	private Message message;
	/** message controller */
	private Object handler;
	/** target method of the controller */
	private Method method;
	/**arguments passed to the method */
	private Object[] params;

	public static MessageTask valueOf(int distributeKey, Object handler,
			Method method, Object[] params,long accountId) {
		MessageTask msgTask = new MessageTask();
		msgTask.distributeKey = distributeKey;
		msgTask.handler = handler;
		msgTask.method  = method;
		msgTask.params  = params;
        msgTask.accountId = accountId;
		return msgTask;
	}

	@Override
	public void action() {
		IdSession session = SessionManager.INSTANCE.getSessionBy(accountId);
		try{
			Object response = method.invoke(handler, params);
			if (response != null) {
				session.sendPacket(message);
			}
		}catch(Exception e){
		    Throwable throwable = e.getCause();
			if(throwable instanceof BizException){
				BizException bizException = (BizException) throwable;
				ErrorMessage errorMessage = new ErrorMessage(bizException.getErrorCode().code(),bizException.getMessage());
				session.sendPacket(errorMessage);
			}
			logger.error("message task execute failed ", e);
		}
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

    public Long getAccountId() {
        return accountId;
    }
}
