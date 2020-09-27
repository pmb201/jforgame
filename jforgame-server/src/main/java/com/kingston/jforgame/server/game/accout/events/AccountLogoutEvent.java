package com.kingston.jforgame.server.game.accout.events;

import com.kingston.jforgame.server.listener.BaseUserEvent;
import com.kingston.jforgame.server.listener.EventType;

/**
 * @Author puMengBin
 * @Date 2020-09-26 17:48
 * @Description
 */
public class AccountLogoutEvent extends BaseUserEvent {


    public AccountLogoutEvent(EventType evtType, Long accountId) {
        super(evtType, accountId);
    }
}
