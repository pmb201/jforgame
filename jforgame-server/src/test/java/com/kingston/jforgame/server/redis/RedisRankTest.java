package com.kingston.jforgame.server.redis;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.kingston.jforgame.server.game.crossrank.CrossRank;
import com.kingston.jforgame.server.game.crossrank.CrossRankKinds;
import com.kingston.jforgame.server.game.crossrank.CrossRankService;
import com.kingston.jforgame.server.game.crossrank.impl.CrossLevelRank;
import com.kingston.jforgame.server.redis.RedisCluster;

public class RedisRankTest {
	
	@Test
	public void test() {
		RedisCluster cluster = RedisCluster.INSTANCE;
		cluster.init();
		cluster.clearAllData();
		CrossRankService rankService = CrossRankService.getInstance();
		
		final int N_RECORD =  10;
		for (int i=1;i<N_RECORD*2;i++) {
			rankService.addRank(new CrossLevelRank(i, 100+i));
		}
		
		List<CrossRank> ranks = rankService.queryRank(CrossRankKinds.LEVEL, 1, N_RECORD);
		for (CrossRank rank:ranks) {
			System.err.println(rank);
		}
		assertTrue(ranks.size() == N_RECORD);
		assertTrue(ranks.get(0).getScore() >= ranks.get(1).getScore());
		
	}
	
}
