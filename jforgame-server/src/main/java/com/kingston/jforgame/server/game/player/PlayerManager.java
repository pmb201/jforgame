package com.kingston.jforgame.server.game.player;

import com.kingston.jforgame.common.utils.NumberUtil;
import com.kingston.jforgame.server.cache.BaseCacheService;
import com.kingston.jforgame.server.db.DbService;
import com.kingston.jforgame.server.db.DbUtils;
import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.entity.Account;
import com.kingston.jforgame.server.game.core.MessagePusher;
import com.kingston.jforgame.server.game.core.SystemParameters;
import com.kingston.jforgame.server.game.database.user.player.Player;
import com.kingston.jforgame.server.game.login.model.Platform;
import com.kingston.jforgame.server.game.player.events.PlayerLogoutEvent;
import com.kingston.jforgame.server.game.player.message.ResCreateNewPlayer;
import com.kingston.jforgame.server.game.player.message.ResKickPlayer;
import com.kingston.jforgame.server.game.player.model.AccountProfile;
import com.kingston.jforgame.server.game.player.model.PlayerProfile;
import com.kingston.jforgame.server.listener.EventDispatcher;
import com.kingston.jforgame.server.listener.EventType;
import com.kingston.jforgame.server.logs.LoggerUtils;
import com.kingston.jforgame.server.net.SessionProperties;
import com.kingston.jforgame.server.utils.IdGenerator;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 玩家业务管理器
 * 
 * @author kingston
 */
public class PlayerManager extends BaseCacheService<Long, Player> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static PlayerManager instance = new PlayerManager();

	private ConcurrentMap<Long, Player> onlines = new ConcurrentHashMap<>();

	/** 全服所有角色的简况 */
	private ConcurrentMap<Long, PlayerProfile> playerProfiles = new ConcurrentHashMap<>();

	/** 全服所有账号的简况 */
	private ConcurrentMap<Long, AccountProfile> accountProfiles = new ConcurrentHashMap<>();

	public void loadAllPlayerProfiles() {
		String sql = "SELECT id, accountId,name,level,job FROM player";
		try {
			List<Map<String, Object>> result = DbUtils.queryMapList(DbUtils.DB_USER, sql);
			for (Map<String, Object> record : result) {
				PlayerProfile baseInfo = new PlayerProfile();
				baseInfo.setAccountId(NumberUtil.longValue(record.get("accountId")));
				baseInfo.setId(NumberUtil.longValue(record.get("id")));
				baseInfo.setJob(NumberUtil.intValue(record.get("job")));
				baseInfo.setName((String) record.get("name"));
				addPlayerProfile(baseInfo);
			}
		} catch (SQLException e) {
			LoggerUtils.error("", e);
		}
	}

	private void addPlayerProfile(PlayerProfile baseInfo) {
		playerProfiles.put(baseInfo.getId(), baseInfo);

		long accountId = baseInfo.getAccountId();
		// 必须将account加载并缓存
        Account account = GameContext.getAccountManager().get(accountId);
		accountProfiles.putIfAbsent(accountId, new AccountProfile());
		AccountProfile accountProfile = accountProfiles.get(accountId);
		accountProfile.addPlayerProfile(baseInfo);
	}

	public AccountProfile getAccountProfiles(long accountId) {
		AccountProfile accountProfile = accountProfiles.get(accountId);
		if (accountProfile != null) {
			return accountProfile;
		}
        Account account = GameContext.getAccountManager().get(accountId);
		if (account != null) {
			accountProfile = new AccountProfile();
			accountProfile.setAccountId(accountId);
			accountProfiles.putIfAbsent(accountId, accountProfile);
		}
		return accountProfile;
	}

	public void addAccountProfile(Account account) {
		long accountId = account.getId();
		if (accountProfiles.containsKey(accountId)) {
			throw new RuntimeException("账号重复-->" + accountId);
		}
		AccountProfile accountProfile = new AccountProfile();
		accountProfile.setAccountId(accountId);
		accountProfiles.put(accountId, accountProfile);
	}

	public List<PlayerProfile> getPlayersBy(long accountId) {
		AccountProfile account = accountProfiles.get(accountId);
		if (account == null) {
			return null;
		}
		return account.getPlayers();
	}

	public void createNewPlayer(IdSession session, String name) {
		long accountId = (long) session.getAttribute(SessionProperties.ACCOUNT);
		Player player = new Player();
		player.setId(IdGenerator.getNextId());
		player.setName(name);
		player.setAccountId(accountId);
		player.setPlatform(Platform.ANDROID);

		long playerId = player.getId();
		// 手动放入缓存
		super.put(playerId, player);

		DbService.getInstance().insertOrUpdate(player);

		PlayerProfile baseInfo = new PlayerProfile();
		baseInfo.setAccountId(accountId);
		baseInfo.setId(playerId);
		baseInfo.setLevel(player.getLevel());
		baseInfo.setJob(player.getJob());
		baseInfo.setName(player.getName());

		ResCreateNewPlayer response = new ResCreateNewPlayer();
		response.setPlayerId(playerId);
		MessagePusher.pushMessage(session, response);

		GameContext.getLoginManager().handleSelectPlayer(session, playerId);
	}

	/**
	 * 从用户表里读取玩家数据
	 */
	@Override
	public Player load(Long playerId) throws Exception {
		String sql = "SELECT * FROM Player where Id = ? ";
//		sql = MessageFormat.format(sql, String.valueOf(playerId));
		Player player = DbUtils.queryOneById(DbUtils.DB_USER, sql, Player.class, String.valueOf(playerId));
		if (player != null) {
			player.doAfterInit();
		}
		return player;
	}

	public Player getOnlinePlayer(long playerId) {
		if (!onlines.containsKey(playerId)) {
			return null;
		}
		return get(playerId);
	}

	/**
	 * 添加进在线列表
	 * 
	 * @param player
	 */
	public void add2Online(Player player) {
		this.onlines.put(player.getId(), player);
	}

	public boolean isOnline(long playerId) {
		return this.onlines.containsKey(playerId);
	}

	/**
	 * 返回在线玩家列表的拷贝
	 * 
	 * @return
	 */
	public ConcurrentMap<Long, Player> getOnlinePlayers() {
		return new ConcurrentHashMap<>(this.onlines);
	}

	/**
	 * 从在线列表中移除
	 * 
	 * @param player
	 */
	public void removeFromOnline(Player player) {
		if (player != null) {
			this.onlines.remove(player.getId());
		}
	}

	public void checkDailyReset(Player player) {
		long resetTimestamp = SystemParameters.dailyResetTimestamp;
		if (player.getLastDailyReset() < resetTimestamp) {
			player.setLastDailyReset(SystemParameters.dailyResetTimestamp);
			onDailyReset(player);
		}
	}

	/**
	 * 各个模块的业务日重置
	 * 
	 * @param player
	 */
	private void onDailyReset(Player player) {

	}

	public void playerLogout(long playerId) {
		Player player = GameContext.getPlayerManager().get(playerId);
		if (player == null) {
			return;
		}
		logger.info("角色[{}]退出游戏", playerId);

		EventDispatcher.getInstance().fireEvent(new PlayerLogoutEvent(EventType.LOGOUT, playerId));
	}

	public void kickPlayer(long playerId) {
		Player player = GameContext.getPlayerManager().getOnlinePlayer(playerId);
		if (player == null) {
			return;
		}
		removeFromOnline(player);
		IdSession session = SessionManager.INSTANCE.getSessionBy(playerId);
		MessagePusher.pushMessage(session, new ResKickPlayer());
//		session.close(false);
	}

}
