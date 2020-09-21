package com.kingston.jforgame.server.game.cross.ladder.service;

import com.kingston.jforgame.server.ServerConfig;
import com.kingston.jforgame.server.cross.core.match.AbstractMatchMessage;
import com.kingston.jforgame.server.cross.core.match.MatchHttpUtil;
import com.kingston.jforgame.server.game.core.SchedulerManager;
import com.kingston.jforgame.server.game.cross.ladder.message.Req_F2M_HeatBeat;
import com.kingston.jforgame.server.logs.LoggerUtils;

/**
 * 天梯战斗服业务处理
 *
 */
public class LadderFightManager {

	private static volatile LadderFightManager self = new LadderFightManager();
	
	public static LadderFightManager getInstance() {
		return self;
	}
	
	public void init() {
		ServerConfig config = ServerConfig.getInstance();
		if (!config.isFight()) {
			return;
		}
		heatBeatTimer();
	}
	
	private void heatBeatTimer() {
		Runnable task = () -> {
			Req_F2M_HeatBeat heatBeat = new Req_F2M_HeatBeat();
			try {
				AbstractMatchMessage response = MatchHttpUtil.submit(heatBeat);
			} catch (Exception e) {
				LoggerUtils.error("发送心跳失败", e);
			}
		};
		SchedulerManager.getInstance().scheduleAtFixedRate(task, 0, 10000);
	}
	
}
