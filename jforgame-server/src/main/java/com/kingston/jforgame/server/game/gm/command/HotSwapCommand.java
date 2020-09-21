package com.kingston.jforgame.server.game.gm.command;

import java.util.List;

import com.kingston.jforgame.server.doctor.HotswapManager;
import com.kingston.jforgame.server.game.database.user.player.Player;
import com.kingston.jforgame.server.game.gm.message.ResGmResult;

public class HotSwapCommand extends AbstractGmCommand {

	@Override
	public String getPattern() {
		return "^hotSwap\\s+([a-zA-Z_0-9]+)";
	}

	@Override
	public String help() {
		return "热更代码(^hotSwap [dirtory])";
	}

	@Override
	public ResGmResult execute(Player player, List<String> params) {
		String path = params.get(0);
		String execResult = HotswapManager.INSTANCE.reloadClass(path);
		return ResGmResult.buildSuccResult(execResult);
	}

}