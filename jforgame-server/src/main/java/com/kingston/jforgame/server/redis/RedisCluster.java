package com.kingston.jforgame.server.redis;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.kingston.jforgame.server.ServerConfig;
import com.kingston.jforgame.server.logs.LoggerUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

public enum RedisCluster {

	/** 枚举单例 */
	INSTANCE;

	private JedisCluster cluster;

	private Jedis jedis;

	public void init() {
		String url = ServerConfig.getInstance().getRedisUrl();
		//local environment, close it!!
		if (StringUtils.isEmpty(url)) {
			return;
		}
		String[] hostPort = url.split(":");
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(50);
		poolConfig.setMinIdle(1);
		poolConfig.setMaxIdle(10);
		JedisPool pool = new JedisPool(poolConfig,hostPort[0],Integer.valueOf(hostPort[1]),2000);
		this.jedis = new Jedis("118.31.236.188", 6390);
		jedis.setDataSource(pool);
	}

	public void destory() {
		jedis.close();
	}

	private TreeSet<String> keys(String pattern){
		TreeSet<String> keys = new TreeSet<>();
		//获取所有的节点
		Map<String, JedisPool> clusterNodes = cluster.getClusterNodes();

		//遍历节点 获取所有符合条件的KEY
		for (String k : clusterNodes.keySet()) {
			JedisPool jp = clusterNodes.get(k);
			Jedis connection = jp.getResource();
			try {
				keys.addAll(connection.keys(pattern));
			} catch(Exception e) {
			} finally{
				connection.close();//用完一定要close这个链接！！！
			}
		}
		return keys;
	}

	public void clearAllData() {
		TreeSet<String> keys=keys("*");
		//遍历key  进行删除  可以用多线程
		for(String key:keys){
			cluster.del(key);
		}
	}

	public Double zscore(String key, String member) {
		try {
			return jedis.zscore(key, member);
		} catch (JedisException e) {
			LoggerUtils.error("", e);
			throw new JedisException(e);
		}
	}

	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		try {
			return jedis.zrangeWithScores(key, start, end);
		} catch (JedisException e) {
			LoggerUtils.error("", e);
			throw new JedisException(e);
		}
	}

	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		try {
			return jedis.zrevrangeWithScores(key, start, end);
		} catch (JedisException e) {
			LoggerUtils.error("", e);
			return new HashSet<>(0);
		}
	}

	public Double zincrby(String key, double score, String member) {
		try {
			return jedis.zincrby(key, score, member);
		} catch (JedisException e) {
			LoggerUtils.error("", e);
			return null;
		}
	}

	public Long zrank(String key, String member) {
		try {
			return jedis.zrank(key, member);
		} catch (JedisException e) {
			LoggerUtils.error("", e);
			return -1L;
		}
	}

	public long hset(String key, String field, String value) {
		try {
			return jedis.hset(key, field, value);
		} catch (JedisException e) {
			LoggerUtils.error("", e);
		}
		return -1L;
	}

	public String hget(String key, String field) {
		try {
			return jedis.hget(key, field);
		} catch (JedisException e) {
			LoggerUtils.error("", e);
			return null;
		}
	}

	/**
	 * @Description: 判断某个key是否存在
	 * @Author: PuMengBin
	 * @Date: 2020-09-24 16:57
	 * @param key:
	 boolean
	 **/
	public boolean exists(String key){
		return jedis.exists(key);
	}

	/**
	 * @Description: 对key自增并返回
	 * @Author: PuMengBin
	 * @Date: 2020-09-24 17:04
	 * @param key:
	 long
	 **/
	public long getId(String key){
		if(jedis.exists(key)){
			return jedis.incr(key);
		}else{
			LocalDate localDate = LocalDate.now();
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime end = localDate.plusDays(1).atStartOfDay();
			long seconds = now.until(end, ChronoUnit.SECONDS);
			jedis.expire(key, (int) seconds);
			return jedis.incr(key);
		}
	}





}
