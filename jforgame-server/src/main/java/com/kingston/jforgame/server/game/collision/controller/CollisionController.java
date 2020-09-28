package com.kingston.jforgame.server.game.collision.controller;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.collision.message.req.ReqCreateRoom;
import com.kingston.jforgame.server.game.collision.message.req.ReqJoinRoom;
import com.kingston.jforgame.server.game.collision.message.req.ReqLeaveRoom;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.annotation.Controller;
import com.kingston.jforgame.socket.annotation.RequestMapping;

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


}
