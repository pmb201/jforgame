package com.kingston.jforgame.server.net.mina.filter;

import com.google.gson.Gson;
import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.logs.LoggerUtils;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.mina.MinaSessionProperties;
import com.kingston.jforgame.socket.session.SessionManager;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class MessageTraceFilter extends IoFilterAdapter {

	private Logger logger = LoggerFactory.getLogger(MessageTraceFilter.class);

	private boolean debug = true;

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		if (debug && traceRequest(message)) {
			logger.error("<<<<<<<<<<[{}]{}={}",
					getMessageSignure(session),
					message.getClass().getSimpleName(), new Gson().toJson(message));
		}
		nextFilter.messageReceived(session, message);
	}

	private boolean traceRequest(Object message) {
		Set<Class<?>> ignores = new HashSet<>();

		return ! ignores.contains(message.getClass());
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		Object message = writeRequest.getMessage();
		if (debug && traceResponse(message)) {
			LoggerUtils.error(">>>>>>>>>>[{}]{}={}",
					getMessageSignure(session),
					message.getClass().getSimpleName(),
					new Gson().toJson(message));
		}
		nextFilter.messageSent(session, writeRequest);
	}

	private boolean traceResponse(Object message) {
		Set<Class<?>> ignores = new HashSet<>();

		return ! ignores.contains(message.getClass());
	}

	private String getMessageSignure(IoSession session) {
		IdSession userSession = SessionManager.INSTANCE.getSessionAttr(session, MinaSessionProperties.UserSession, IdSession.class);
		long accountId = SessionManager.INSTANCE.getAccountIdBy(userSession);
		if (accountId > 0) {
            AccountProfile accountProfile = GameContext.getAccountManager().getOnlineUser(accountId);
			return accountProfile.getName();
		}
		return String.valueOf(session);
	}

}
