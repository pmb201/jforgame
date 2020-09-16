package com.kingston.jforgame.server.game.admin.commands;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.admin.http.*;
import com.kingston.jforgame.server.logs.LoggerSystem;

/**
 * @Author puMengBin
 * @Date 2020-09-07 20:38
 * @Description
 */
@CommandHandler(cmd= HttpCommands.HOT_SWAP_PATH)
public class HotSwapPathCommandHandler extends HttpCommandHandler {
    @Override
    public HttpCommandResponse action(HttpCommandParams httpParams) {
        GameContext.getHotswapManager().reloadClass(httpParams.getString("path"));
        LoggerSystem.HTTP_COMMAND.getLogger().info("收到后台命令，执行热部署目录");
        return HttpCommandResponse.valueOfSucc();
    }
}
