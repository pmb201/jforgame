package com.kingston.jforgame.server.game.login.controller;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.login.message.req.ReqAccountLogin;
import com.kingston.jforgame.server.game.login.message.req.ReqSelectPlayer;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.annotation.Controller;
import com.kingston.jforgame.socket.annotation.RequestMapping;

@Controller
public class LoginController {

	@RequestMapping
	public void reqAccountLogin(IdSession session, ReqAccountLogin request) {
        GameContext.getLoginManager().handleAccountLogin(session, request.getAccountId(), request.getPassword());
	}

	@RequestMapping
	public void reqSelectPlayer(IdSession session, ReqSelectPlayer requst) {
        GameContext.getLoginManager().handleSelectPlayer(session, requst.getPlayerId());
	}

}
