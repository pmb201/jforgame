package com.kingston.jforgame.server.game.database.config.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.kingston.jforgame.server.db.DbUtils;
import com.kingston.jforgame.server.game.database.config.Reloadable;
import com.kingston.jforgame.server.game.database.config.bean.ConfigPlayerLevel;
import com.kingston.jforgame.server.logs.LoggerUtils;

/**
 * 玩家等级配置表
 * 
 * @author kingston
 */
public class ConfigPlayerLevelStorage implements Reloadable {

	private Map<Integer, ConfigPlayerLevel> levels = new HashMap<>();

	@Override
	public void reload() {
		String sql = "SELECT * FROM ConfigPlayerLevel";
		try {
			List<ConfigPlayerLevel> datas = DbUtils.queryMany(DbUtils.DB_DATA, sql, ConfigPlayerLevel.class);
			levels = datas.stream().collect(Collectors.toMap(ConfigPlayerLevel::getLevel, Function.identity()));
		} catch (Exception e) {
			LoggerUtils.error("", e);
		}
	}

	public ConfigPlayerLevel getConfigBy(int level) {
		return levels.get(level);
	}

}
