package com.kingston.jforgame.server.game.function.facade;

import com.kingston.jforgame.server.game.player.events.PlayerLevelUpEvent;
import com.kingston.jforgame.server.listener.EventType;
import com.kingston.jforgame.server.listener.annotation.EventHandler;
import com.kingston.jforgame.server.listener.annotation.Listener;

@Listener
public class FunctionListener {

	@EventHandler(value=EventType.PLAYER_LEVEL_UP)
	public void onPlayerLevelup(PlayerLevelUpEvent levelUpEvent) {
		long playerId = levelUpEvent.getPlayerId();
        //Player player = GameContext.getPlayerManager().get(playerId);
		/*Set<Integer> openFuncs = player.getFunction().getFuncs();

		ConfigFunctionStorage functionStorage = ConfigDataPool.getInstance().getStorage(ConfigFunctionStorage.class);
		List<ConfigFunction> openByLevelFuncs = functionStorage.getFunctionBy(OpenType.LEVEL);

		int level = player.getLevel();
		for (ConfigFunction configFunc : openByLevelFuncs) {
			int funcId = configFunc.getId();
			if (! openFuncs.contains(funcId)) {
				if (level >= configFunc.getOpenTarget()) {
					player.getFunction().open(funcId);
				}
			}
		}*/
	}

}
