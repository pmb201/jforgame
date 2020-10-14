package com.kingston.jforgame.server.game.gm.command;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.entity.Account;
import com.kingston.jforgame.server.game.gm.message.ResGmResult;

import java.util.List;

/**
 * @Author puMengBin
 * @Date 2020-10-13 16:42
 * @Description
 */
public class CollisionGameEndCommand extends AbstractGmCommand{
    @Override
    public String getPattern() {
        return "^collisionGameEnd\\s+([0-9]+)";
    }

    @Override
    public String help() {
        return "游戏结束：(^collisionGameEnd [roomId])";
    }

    @Override
    public ResGmResult execute(Account account, List<String> params) {
        Long roomId = Long.parseLong(params.get(0));
        GameContext.getCollisionManager().endGame(roomId);
        return null;
    }
}
