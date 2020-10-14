
package com.kingston.jforgame.server.client;

import com.kingston.jforgame.orm.OrmBridge;
import com.kingston.jforgame.orm.OrmProcessor;
import com.kingston.jforgame.orm.SqlFactory;
import com.kingston.jforgame.server.ServerConfig;
import com.kingston.jforgame.server.ServerScanPaths;
import com.kingston.jforgame.server.db.DbService;
import com.kingston.jforgame.server.db.DbUtils;
import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.game.room.model.RoomProfile;
import com.kingston.jforgame.server.redis.RedisCluster;
import com.kingston.jforgame.socket.message.MessageFactory;

/**
 * 客户端模拟器启动程序
 * @author kingston
 */
public class ClientStartup {


	public static void main(String[] args) throws Exception {
		//初始化协议池
		MessageFactory.INSTANCE.initMessagePool(ServerScanPaths.MESSAGE_PATH);
		//读取服务器配置
		ServerConfig.getInstance();
		RedisCluster.INSTANCE.init();
		DbService.getInstance().init();
		OrmProcessor.INSTANCE.initOrmBridges("com.kingston.jforgame.server");
		AccountProfile accountProfile = new AccountProfile();
		accountProfile.setMomoId("1");
		accountProfile.setId(2L);
		accountProfile.setRoomId(1);
		//GameContext.getRoomManager().createRoom("1",accountProfile);
		DbUtils.init();
		//存储房间信息
		OrmBridge ormBridge = OrmProcessor.INSTANCE.getOrmBridge(RoomProfile.class);
		String sql = SqlFactory.createInsertSql(GameContext.getRoomManager().getByRoomId(290018L),ormBridge);
		boolean b = DbUtils.executeSql(sql);
		System.out.println(b);

		//DbService.getInstance().insertOrUpdate(GameContext.getRoomManager().getByRoomId(1L));

//		ClientPlayer robot = new ClientPlayer("kingston");
//		robot.buildConnection();
//		robot.login();
//		robot.selectedPlayer(10000L);
		
//		ReqGmExecMessage req = new ReqGmExecMessage();
//		req.setCommand("reloadConfig configactivity");
//		robot.sendMessage(req);
//		robot.createNew();
	}


}
