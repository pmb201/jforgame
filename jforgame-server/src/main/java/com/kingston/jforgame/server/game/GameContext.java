package com.kingston.jforgame.server.game;

import com.kingston.jforgame.server.doctor.HotswapManager;
import com.kingston.jforgame.server.game.accout.AccountManager;
import com.kingston.jforgame.server.game.chat.ChatManager;
import com.kingston.jforgame.server.game.collision.CollisionManager;
import com.kingston.jforgame.server.game.gm.GmManager;
import com.kingston.jforgame.server.game.login.LoginManager;
import com.kingston.jforgame.server.game.person.PersonManager;
import com.kingston.jforgame.server.game.room.RoomManager;
import com.kingston.jforgame.server.game.skill.SkillManager;

/**
 * 游戏业务上下文
 * 管理game包下所有manager
 */
public class GameContext {

    private static AccountManager accountManager = new AccountManager();

    public static AccountManager getAccountManager() {
        return accountManager;
    }

    private static GmManager gmManager = new GmManager();

    public static GmManager getGmManager() {
        return gmManager;
    }

    private static LoginManager loginManager = new LoginManager();

    public static LoginManager getLoginManager() {
        return loginManager;
    }

    /*private static PlayerManager playerManager = new PlayerManager();

    public static PlayerManager getPlayerManager() {
        return playerManager;
    }*/

    private static RoomManager roomManager = new RoomManager();

    public static RoomManager getRoomManager(){
        return roomManager;
    }

    private static SkillManager skillManager = new SkillManager();

    public static SkillManager getSkillManager() {
        return skillManager;
    }

    private static ChatManager chatManager;

    public static ChatManager getChatManager() {
        return chatManager;
    }

    private static PersonManager personManager = new PersonManager();

    public static PersonManager getPersonManager(){return personManager;}

    private static HotswapManager hotswapManager = HotswapManager.INSTANCE;

    public static HotswapManager getHotswapManager(){return hotswapManager;}

    public static CollisionManager collisionManager = new CollisionManager();

    public static CollisionManager getCollisionManager(){return collisionManager;}


}
