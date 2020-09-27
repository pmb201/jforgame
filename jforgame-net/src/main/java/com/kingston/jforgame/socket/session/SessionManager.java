package com.kingston.jforgame.socket.session;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;

import com.kingston.jforgame.socket.IdSession;

public enum SessionManager {

	/** 枚举单例 */
	INSTANCE;

	/** distributeKey auto generator  */
	private AtomicInteger distributeKeyGenerator = new AtomicInteger();
	/** key=accountId, value=session */
	private ConcurrentMap<Long, IdSession> account2sessions = new ConcurrentHashMap<>();


	public void bindAccount(long accountId, IdSession session) {
		//biding playeId to session
		session.setAttribute(IdSession.ID, accountId);
		this.account2sessions.put(accountId, session);
	}

	/**
	 * get session's playerId
	 * @param session
	 * @return
	 */
	public long getAccountIdBy(IdSession session) {
		if (session != null) {
			return session.getOwnerId();
		}
		return 0;
	}

	public IdSession getSessionBy(long accountId) {
		return account2sessions.get(accountId);
	}

	/**
	 * get appointed sessionAttr
	 */
	@SuppressWarnings("unchecked")
	public <T> T getSessionAttr(IoSession session, AttributeKey attrKey, Class<T> attrType) {
		return (T)session.getAttribute(attrKey);
	}

	public int getNextDistributeKey() {
		return this.distributeKeyGenerator.getAndIncrement();
	}

	public String getRemoteIp(IoSession session) {
		return ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
	}

}
