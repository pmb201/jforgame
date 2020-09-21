package com.kingston.jforgame.server.game.cross.ladder.service;

import java.io.IOException;

import com.kingston.jforgame.server.ServerConfig;
import com.kingston.jforgame.server.cross.core.client.C2SSessionPoolFactory;
import com.kingston.jforgame.server.cross.core.client.CCSession;
import com.kingston.jforgame.server.cross.core.match.MatchHttpUtil;
import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.cross.ladder.message.Req_G2F_LadderTransfer;
import com.kingston.jforgame.server.game.cross.ladder.message.Req_G2M_LadderApply;
import com.kingston.jforgame.server.game.cross.ladder.message.vo.LadderMatchVo;
import com.kingston.jforgame.server.game.cross.ladder.utils.CrossJsonUtil;
import com.kingston.jforgame.server.game.database.user.player.Player;
import com.kingston.jforgame.server.logs.LoggerUtils;

/**
 * 天梯游戏服（客户端）业务处理
 *
 */
public class LadderClientManager {

	private static volatile LadderClientManager self = new LadderClientManager();
	
	public static LadderClientManager getInstance() {
		return self;
	}
	
	public void init() {
		ServerConfig config = ServerConfig.getInstance();
		if (!config.isFight()) {
			return;
		}
	}
	
	
	public void apply(long playerId) {
        Player player = GameContext.getPlayerManager().get(playerId);
		// 一堆业务判断
		Req_G2M_LadderApply apply = new Req_G2M_LadderApply();
		apply.setPlayerId(playerId);
		apply.setPower(100);
		apply.setScore(100);
		try {
			MatchHttpUtil.submit(apply);
		} catch (IOException e) {
			LoggerUtils.error("天梯报错异常，玩家:" + playerId, e);
			return;
		}
	}
	
	private void handleFight(LadderMatchVo matchVo) {
		int selfServerId = ServerConfig.getInstance().getServerId();
		if (matchVo.getBlueServerId() == selfServerId) {
            Player player = GameContext.getPlayerManager().get(matchVo.getBluePlayerId());
			transferToFight(player, matchVo);
		}
		if (matchVo.getRedServerId() == selfServerId) {
            Player player = GameContext.getPlayerManager().get(matchVo.getRedPlayerId());
			transferToFight(player, matchVo);
		}
	}
	
	/**
	 * 将玩家从游戏服传到战斗服
	 * @param player
	 */
	private void transferToFight(Player player, LadderMatchVo matchVo) {
		String fightIp = matchVo.getFightServerIp();
		int fightPort = matchVo.getFightServerPort();
		
		CCSession session = C2SSessionPoolFactory.getInstance().borrowSession(fightIp, fightPort);
		if (session != null) {
			// 将玩家数据打包后发到战斗服
			Req_G2F_LadderTransfer rpcTransfer = new Req_G2F_LadderTransfer();
			String playerJson = CrossJsonUtil.object2String(player);
			session.sendMessage(rpcTransfer);
			// 通知客户端切换socket到战斗服
		}
	}
	
}
