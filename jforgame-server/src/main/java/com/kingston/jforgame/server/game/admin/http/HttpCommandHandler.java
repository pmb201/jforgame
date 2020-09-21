package com.kingston.jforgame.server.game.admin.http;

/**
 * 抽象后台命令处理者
 * @author kingston
 */
public abstract class HttpCommandHandler {
	
	/**
	 * 处理后台命令
	 * @param httpParams
	 * @return
	 */
	public abstract HttpCommandResponse action(HttpCommandParams httpParams);

}
