package com.kingston.jforgame.server.game.admin.commands;
import com.kingston.jforgame.server.game.admin.http.*;

import java.util.Map;

@CommandHandler(cmd=HttpCommands.KICK_PLAYER)
public class KickPlayerCommand extends HttpCommandHandler {

	@Override
	public HttpCommandResponse action(HttpCommandParams httpParams) {
		Map<String, String> params = httpParams.getParams();
		String key = "player";
		if (params.containsKey(key)) {
			long playeId = Long.parseLong(params.get(key));
            //GameContext.getPlayerManager().kickPlayer(playeId);
			return HttpCommandResponse.valueOfSucc();
		}
		return HttpCommandResponse.valueOfSucc();
	}

}
