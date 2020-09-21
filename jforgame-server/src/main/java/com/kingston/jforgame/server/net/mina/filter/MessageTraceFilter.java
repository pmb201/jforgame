package com.kingston.jforgame.server.net.mina.filter;

import java.util.HashSet;
import java.util.Set;

import com.kingston.jforgame.server.game.GameContext;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.kingston.jforgame.server.game.database.user.player.Player;
import com.kingston.jforgame.server.logs.LoggerUtils;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.mina.MinaSessionProperties;
import com.kingston.jforgame.socket.session.SessionManager;

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
		long playerId = SessionManager.INSTANCE.getPlayerIdBy(userSession);
		if (playerId > 0) {
            Player player = GameContext.getPlayerManager().getOnlinePlayer(playerId);
			return player.getName();
		}
		return String.valueOf(session);
	}

}
