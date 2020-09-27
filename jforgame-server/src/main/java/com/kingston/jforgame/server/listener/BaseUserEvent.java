package com.kingston.jforgame.server.listener;

import lombok.Getter;

/**
 * @Author puMengBin
 * @Date 2020-09-26 17:45
 * @Description
 */
public abstract class BaseUserEvent extends BaseGameEvent{

    /** 用户Id */
    @Getter
    private Long accountId;

    public BaseUserEvent(EventType evtType,Long accountId) {
        super(evtType);
        this.accountId = accountId;
    }
}
