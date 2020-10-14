package com.kingston.jforgame.server.game.login.controller;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.login.message.req.ReqAccountLogin;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.annotation.Controller;
import com.kingston.jforgame.socket.annotation.RequestMapping;

@Controller
public class LoginController {

	@RequestMapping
	public void reqAccountLogin(IdSession session, ReqAccountLogin request) throws Exception {
        GameContext.getLoginManager().handleAccountLogin(session, request);
	}


}
