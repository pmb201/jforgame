package com.kingston.jforgame.server.game.person.controller;

import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.person.message.req.ReqPersonToString;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.annotation.Controller;
import com.kingston.jforgame.socket.annotation.RequestMapping;

/**
 * @Author puMengBin
 * @Date 2020-09-07 19:14
 * @Description
 */
@Controller
public class PersonController {

    @RequestMapping
    public void personToString(IdSession session, ReqPersonToString req) {
        GameContext.getPersonManager().personToString(req);
    }

}
