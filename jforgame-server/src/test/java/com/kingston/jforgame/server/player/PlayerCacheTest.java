package com.kingston.jforgame.server.player;

import com.kingston.jforgame.orm.OrmProcessor;
import com.kingston.jforgame.server.ServerScanPaths;
import com.kingston.jforgame.server.db.DbUtils;
import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.entity.Account;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * 测试玩家缓存系统
 * @author kingston
 */
public class PlayerCacheTest {

	@Before
	public void init() {
		//初始化orm框架
		OrmProcessor.INSTANCE.initOrmBridges(ServerScanPaths.ORM_PATH);
		//初始化数据库连接池
		DbUtils.init();
	}

	@Test
	public void testQueryPlayer() {
		long playerId = 10000L;
		//预先保证用户数据表playerId = 10000的数据存在
        Account player = GameContext.getAccountManager().get(playerId);
		//改变内存里的玩家名称
		player.setName("newPlayerName");
		//内存里玩家的新名称
		String playerName = player.getName();
		//通过同一个id再次获取玩家数据
        Account player2 = GameContext.getAccountManager().get(playerId);
		//验证新的玩家就是内存里的玩家，因为如果又是从数据库里读取，那么名称肯定跟内存的不同！！
		assertTrue(playerName.equals(player2.getName()));
	}

}
