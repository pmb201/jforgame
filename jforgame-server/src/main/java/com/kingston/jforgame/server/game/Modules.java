
package com.kingston.jforgame.server.game;

public interface Modules {

	// ------------------底层功能支持模块（从0到100）-----------------

	int BASE = 1;

	int GM = 2;

	int NOTICE = 3;

	int ERROR = 4;

	// ------------------业务功能模块（101开始）---------------------

	/** 登录 */
	int LOGIN = 101;
	/** 玩家 */
	int PLAYER = 102;
	/** 场景 */
	int SCENE = 103;
	/** 活动 */
	int ACTIVITY = 104;
	/** 技能 */
	int SKILL = 105;
	/** 聊天 */
	int CHAT = 106;
	/** person */
	int PERSON = 108;
	/** 碰撞游戏 */
	int COLLISION = 109;

	// ------------------跨服业务功能模块（501开始）---------------------
	/** 跨服天梯 */
	int LADDER = 501;




}
