package com.kingston.jforgame.server.game.accout;

import com.google.gson.Gson;
import com.kingston.jforgame.common.utils.HttpUtil;
import com.kingston.jforgame.server.ServerConfig;
import com.kingston.jforgame.server.cache.BaseCacheService;
import com.kingston.jforgame.server.db.DbService;
import com.kingston.jforgame.server.db.DbUtils;
import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.entity.Account;
import com.kingston.jforgame.server.game.accout.events.AccountLogoutEvent;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.game.login.message.req.ReqAccountLogin;
import com.kingston.jforgame.server.game.login.model.QQLoginResult;
import com.kingston.jforgame.server.listener.EventDispatcher;
import com.kingston.jforgame.server.listener.EventType;
import com.kingston.jforgame.server.logs.LoggerUtils;
import com.kingston.jforgame.server.utils.IdGenerator;
import com.kingston.jforgame.socket.IdSession;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AccountManager extends BaseCacheService<Long, Account>{

	private static final String APP_SECRET = "";

	private ConcurrentMap<Long, AccountProfile> onlines = new ConcurrentHashMap<>();

	/** 全服所有账号的简况 */
	private ConcurrentMap<String, Account> accountConcurrentMap = new ConcurrentHashMap<>();


	public AccountProfile login(IdSession session,ReqAccountLogin reqAccountLogin) throws Exception {


		// 调用QQ登录
		String param = HttpUtil.buildUrlParam("appid",reqAccountLogin.getAppId(),"js_code",reqAccountLogin.getCode(),"secret",APP_SECRET,"grant_type","authorization_code");
		String url = ServerConfig.getInstance().getQqClient() + "?" + param;
		String resultJson = HttpUtil.get(url);
		QQLoginResult qqLoginResult = new Gson().fromJson(resultJson, QQLoginResult.class);


		AccountProfile accountProfile = null;
		Account account = getByUnionId(qqLoginResult.getUnionId());
		if (account != null) {
			accountProfile = account.buildProfile();
		}else{
			account = new Account();
			account.setId(IdGenerator.getNextId());
			account.setUnionId(qqLoginResult.getUnionId());
			DbService.getInstance().insertOrUpdate(account);
			accountProfile = account.buildProfile();
		}
		add2Online(accountProfile);
		accountConcurrentMap.put(accountProfile.getUnionId(),account);
		return accountProfile;
	}



	public AccountProfile getOnlineUser(Long accountId) {
		if (!onlines.containsKey(accountId)) {
			return null;
		}
		return onlines.get(accountId);
	}

	/**
	 * 添加进在线列表
	 *
	 * @param accountProfile
	 */
	public void add2Online(AccountProfile accountProfile) {
		accountProfile.setStatus(AccountProfile.Status.ON_LINE.getCode());
		this.onlines.put(accountProfile.getId(), accountProfile);
	}

	public boolean isOnline(Long accountId) {
		boolean onLine = this.onlines.containsKey(accountId);
		if(onLine){
			AccountProfile accountProfile = onlines.get(accountId);
			if(accountProfile.getStatus() == AccountProfile.Status.OFF_LINE.getCode()){
				accountProfile.setStatus(AccountProfile.Status.ON_LINE.getCode());
			}
		}
		return onLine;
	}

	/**
	 * 返回在线玩家列表的拷贝
	 *
	 * @return
	 */
	public ConcurrentMap<Long, AccountProfile> getOnlineUsers() {
		return new ConcurrentHashMap<>(this.onlines);
	}

	/**
	 * 从在线列表中移除
	 *
	 * @param accountProfile
	 */
	public void removeFromOnline(AccountProfile accountProfile) {
		if (accountProfile != null) {
			this.onlines.remove(accountProfile.getId());
		}
	}

	public void userLogout(Long accountId) {
		AccountProfile accountProfile = GameContext.getAccountManager().getOnlineUser(accountId);
		if (accountProfile == null) {
			return;
		}
		LoggerUtils.warn("用户[{}]退出游戏", accountId);

		EventDispatcher.getInstance().fireEvent(new AccountLogoutEvent(EventType.LOGOUT, accountId));
	}


	@Override
	public Account load(Long accountId) throws Exception {
		String sql = "SELECT * FROM account WHERE id = ? ";
		Account account = DbUtils.queryOneById(DbUtils.DB_USER, sql, Account.class, String.valueOf(accountId));
		return account;
	}

	public Account getByUnionId(String unionId) throws Exception {
		if(accountConcurrentMap.containsKey(unionId)){
			return accountConcurrentMap.get(unionId);
		}
		String sql = "SELECT * FROM account WHERE unionId = ? ";
		Account account = DbUtils.queryOneById(DbUtils.DB_USER, sql, Account.class, String.valueOf(unionId));
		return account;
	}
}
