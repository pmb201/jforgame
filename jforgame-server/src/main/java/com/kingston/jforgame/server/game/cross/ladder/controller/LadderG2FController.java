package com.kingston.jforgame.server.game.cross.ladder.controller;

import com.kingston.jforgame.server.cross.core.server.CrossController;
import com.kingston.jforgame.server.cross.core.server.SCSession;
import com.kingston.jforgame.server.game.cross.ladder.message.Req_G2F_LadderTransfer;
import com.kingston.jforgame.socket.annotation.RequestMapping;

@CrossController
public class LadderG2FController {
	
	@RequestMapping
	public void reqApply(SCSession session, Req_G2F_LadderTransfer req) {
		System.out.println("收到游戏服协议<--" + req);
	}

}
