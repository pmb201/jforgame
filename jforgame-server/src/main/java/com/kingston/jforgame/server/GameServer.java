package com.kingston.jforgame.server;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.listener.ListenerManager;
import com.kingston.jforgame.server.net.MessageDispatcher;
import com.kingston.jforgame.server.net.mina.MinaSocketServer;
import com.kingston.jforgame.server.net.netty.NettySocketServer;
import com.kingston.jforgame.server.net.netty.NettyWebSocketServer;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingston.jforgame.common.utils.TimeUtil;
import com.kingston.jforgame.orm.OrmProcessor;
import com.kingston.jforgame.server.cross.core.CrossServer;
import com.kingston.jforgame.server.db.DbService;
import com.kingston.jforgame.server.db.DbUtils;
import com.kingston.jforgame.server.game.admin.http.HttpCommandManager;
import com.kingston.jforgame.server.game.admin.http.HttpServer;
import com.kingston.jforgame.server.game.core.CronSchedulerHelper;
import com.kingston.jforgame.server.game.core.SystemParameters;
import com.kingston.jforgame.server.game.database.config.ConfigDataPool;
import com.kingston.jforgame.server.monitor.jmx.GameMonitor;
import com.kingston.jforgame.server.monitor.jmx.GameMonitorMBean;
import com.kingston.jforgame.server.redis.RedisCluster;
import com.kingston.jforgame.socket.ServerNode;
import com.kingston.jforgame.socket.message.MessageFactory;
import com.kingston.jforgame.socket.task.TaskHandlerContext;

public class GameServer {

	private static Logger logger = LoggerFactory.getLogger(GameServer.class);

	private static GameServer gameServer = new GameServer();

	private ServerNode socketServer;

	private ServerNode webSocketServer;

	private ServerNode httpServer;

	private ServerNode crossServer;

	public static GameServer getInstance() {
		return gameServer;
	}

	public void start() throws Exception {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		// 游戏基础框架服务启动
		frameworkInit();
		// 游戏业务初始化
		gameLogicInit();

		stopWatch.stop();
		logger.error("游戏服务启动，耗时[{}]毫秒", stopWatch.getTime());

		// mbean监控
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		GameMonitorMBean controller = new GameMonitor();
		mbs.registerMBean(controller, new ObjectName("GameMXBean:name=GameMonitor"));
	}

	private void frameworkInit() throws Exception {
		// 加载服务版本号
		ServerVersion.load();
		// 初始化协议池
		MessageFactory.INSTANCE.initMessagePool(ServerScanPaths.MESSAGE_PATH);
		// 读取服务器配置
		ServerConfig config = ServerConfig.getInstance();
		// 初始化orm框架
		OrmProcessor.INSTANCE.initOrmBridges(ServerScanPaths.ORM_PATH);
		// 初始化数据库连接池
		DbUtils.init();
		// 事件驱动
		ListenerManager.INSTANCE.init();
		// 初始化消息工作线程池
		TaskHandlerContext.INSTANCE.initialize();
		// 初始化job定时任务
		CronSchedulerHelper.initAndStart();
		// 读取所有策划配置
		ConfigDataPool.getInstance().loadAllConfigs();
		// 异步持久化服务
		DbService.getInstance().init();
		// 读取系统参数
		loadSystemRecords();
		// Redis cache
		RedisCluster.INSTANCE.init();
		// http admin commands
		HttpCommandManager.getInstance().initialize(ServerScanPaths.HTTP_ADMIN_PATH);

		GameContext.getGmManager().init();

		if (config.getCrossPort() > 0) {
			// 启动跨服服务
			crossServer = new CrossServer();
			crossServer.start();
		}
		// 启动socket服务
		MessageDispatcher messageDispatcher = new MessageDispatcher();
		//socketServer = new MinaSocketServer(messageDispatcher);
		webSocketServer = new NettyWebSocketServer(config.getMaxReceiveBytes(),messageDispatcher);
		//socketServer.start();
		webSocketServer.start();
		// 启动http服务
		httpServer = new HttpServer();
		httpServer.start();
	}

	private void loadSystemRecords() throws Exception {
		SystemParameters.load();
		// 启动时检查每日重置
		long now = System.currentTimeMillis();
		if (now - SystemParameters.dailyResetTimestamp > TimeUtil.ONE_DAY) {
			logger.info("启动时每日重置");
			SystemParameters.update("dailyResetTimestamp", now);
		}
	}

	private void gameLogicInit() {
		// 游戏启动时，各种业务初始化写在这里吧
		GameContext.getPlayerManager().loadAllPlayerProfiles();
		// 跨服天梯
//		LadderFightManager.getInstance().init();

	}

	public void shutdown() {
		logger.error("游戏进程准备关闭");
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		// 各种业务逻辑的关闭写在这里。。。
		//socketServer.shutdown();
		webSocketServer.shutdown();
		httpServer.shutdown();
		if (crossServer != null) {
			crossServer.shutdown();
		}
		DbService.getInstance().shutDown();
		stopWatch.stop();
		logger.error("游戏服务正常关闭，耗时[{}]毫秒", stopWatch.getTime());
	}

}
