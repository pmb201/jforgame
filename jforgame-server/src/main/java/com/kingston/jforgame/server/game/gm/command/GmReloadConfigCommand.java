package com.kingston.jforgame.server.game.gm.command;

import com.kingston.jforgame.server.game.accout.entity.Account;
import com.kingston.jforgame.server.game.database.config.ConfigDataPool;
import com.kingston.jforgame.server.game.gm.message.ResGmResult;

import java.util.List;

/**
 * 修改配置表的gm
 * 
 * @author kingston
 */
public class GmReloadConfigCommand extends AbstractGmCommand {

	@Override
	public String getPattern() {
		return "^reloadConfig\\s+([a-zA-Z_]+)";
	}

	@Override
	public String help() {
		return "修改配置表(^reloadConfig [tableName])";
	}

	@Override
	public ResGmResult execute(Account account, List<String> params) {
		String tableName = params.get(0);
		if (ConfigDataPool.getInstance().reload(tableName)) {
			return ResGmResult.buildSuccResult("重载" + tableName + "表成功");
		}
		return ResGmResult.buildFailResult("找不到目标配置表");
	}

}
