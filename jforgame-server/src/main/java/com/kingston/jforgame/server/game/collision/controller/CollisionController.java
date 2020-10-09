package com.kingston.jforgame.server.game.collision.controller;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.game.collision.message.req.*;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.annotation.Controller;
import com.kingston.jforgame.socket.annotation.RequestMapping;
import com.kingston.jforgame.socket.session.SessionManager;

/**
 * @Author puMengBin
 * @Date 2020-09-27 19:22
 * @Description
 */
@Controller
public class CollisionController {

    @RequestMapping
    public void createRoom(IdSession session, ReqCreateRoom reqCreateRoom){
        GameContext.getCollisionManager().createRoom(session);
    }

    @RequestMapping
    public void joinRoom(IdSession session, ReqJoinRoom reqJoinRoom){
        GameContext.getCollisionManager().joinRoom(session,reqJoinRoom.getRoomId());
    }

    @RequestMapping
    public void leaveRoom(IdSession session, ReqLeaveRoom leaveRoom){
        GameContext.getCollisionManager().leaveRoom(session);
    }


    @RequestMapping
    public void startGame(IdSession session, ReqStartGame startGame){
        long accountId = SessionManager.INSTANCE.getAccountIdBy(session);
        AccountProfile accountProfile = GameContext.getAccountManager().getOnlineUser(accountId);
        GameContext.getCollisionManager().startGame(accountProfile.getRoomId());
    }

    @RequestMapping
    public void userPosition(IdSession session, ReqUserPosition userPosition){
        GameContext.getCollisionManager().userPosition(session,userPosition);
    }

    @RequestMapping
    public void ready(IdSession session, ReqReady reqReady){
        GameContext.getCollisionManager().ready(session);
    }

}
