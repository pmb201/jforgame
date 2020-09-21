package com.kingston.jforgame.server.game.admin.commands;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.admin.http.*;
import com.kingston.jforgame.server.logs.LoggerSystem;

/**
 * @Author puMengBin
 * @Date 2020-09-07 20:06
 * @Description
 */
@CommandHandler(cmd= HttpCommands.HOT_SWAP_CLASS)
public class HotSwapClassCommandHandler extends HttpCommandHandler {
    @Override
    public HttpCommandResponse action(HttpCommandParams httpParams) {
        GameContext.getHotswapManager().loadJavaFile(httpParams.getString("classFullName"));
        LoggerSystem.HTTP_COMMAND.getLogger().info("收到后台命令，执行热部署类文件");
        return HttpCommandResponse.valueOfSucc();
    }
}
