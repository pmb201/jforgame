package com.kingston.jforgame.server.game.login;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.game.core.MessagePusher;
import com.kingston.jforgame.server.game.gm.message.ResGmResult;
import com.kingston.jforgame.server.game.login.message.res.ResAccountLogin;
import com.kingston.jforgame.server.game.scene.message.ResPlayerEnterScene;
import com.kingston.jforgame.server.net.SessionProperties;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.combine.CombineMessage;
import com.kingston.jforgame.socket.session.SessionManager;

public class LoginManager {


	/**
	 *
	 * @param unionId 用户unionId
	 * @param password  账号密码
	 */
	public void handleAccountLogin(IdSession session, String unionId, String password) throws Exception {
		AccountProfile accountProfile = GameContext.getAccountManager().login(unionId);
		session.setAttribute(SessionProperties.ACCOUNT, accountProfile);
		SessionManager.INSTANCE.bindAccount(accountProfile.getId(),session);

		ResAccountLogin loginMessage = new ResAccountLogin();
		loginMessage.setAccountProfile(accountProfile);
		MessagePusher.pushMessage(session, loginMessage);
		
		if ("kingston".equals(password)) {
			CombineMessage combineMessage = new CombineMessage();
			combineMessage.addMessage(new ResPlayerEnterScene());
			combineMessage.addMessage(ResGmResult.buildSuccResult("执行gm成功"));
			MessagePusher.pushMessage(session, combineMessage);
		}
	}


}
