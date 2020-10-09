package com.kingston.jforgame.server.game.accout.listener;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.AccountManager;
import com.kingston.jforgame.server.game.accout.events.AccountLogoutEvent;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.game.collision.CollisionManager;
import com.kingston.jforgame.server.listener.EventType;
import com.kingston.jforgame.server.listener.annotation.EventHandler;
import com.kingston.jforgame.server.listener.annotation.Listener;

import java.util.List;

/**
 * @Author puMengBin
 * @Date 2020-09-27 14:16
 * @Description
 */
@Listener
public class UserLogoutListener {

    @EventHandler(value= EventType.LOGOUT)
    public void onUserLogout(AccountLogoutEvent logoutEvent) {
        System.err.println(getClass().getSimpleName()+"捕捉到事件"+logoutEvent);
        AccountManager accountManager = GameContext.getAccountManager();
        if(accountManager.isOnline(logoutEvent.getAccountId())){
            AccountProfile accountProfile = accountManager.getOnlineUser(logoutEvent.getAccountId());
            accountManager.removeFromOnline(accountProfile);
            accountProfile.setStatus(AccountProfile.Status.OFF_LINE.getCode());
            List<CollisionManager.AccountId> accountIdList = GameContext.getCollisionManager().accountIds;
            for(CollisionManager.AccountId accountId : accountIdList){
                if(accountId.getId() == accountProfile.getId()){
                    accountId.setUsed(false);
                }
            }
        }
    }
}
