package com.kingston.jforgame.server.cross.core.callback;

import com.kingston.jforgame.server.cross.core.CrossCommands;
import com.kingston.jforgame.server.cross.core.server.SCSession;
import com.kingston.jforgame.socket.annotation.MessageMeta;
import com.kingston.jforgame.socket.message.Message;

import java.util.HashMap;
import java.util.Map;

@MessageMeta(module = CrossCommands.G2C_CALL_BACK)
public abstract class CallbackHandler {

    private static Map<Integer, CallbackHandler> handlers = new HashMap<>();

    private void init() {
        handlers.put(cmdType(), this);
    }

    public abstract void onRequest(SCSession session, CReqCallBack req);

    public void sendBack(SCSession session, CReqCallBack req, Message response) {
        CRespCallBack callBack = CRespCallBack.valueOf(response);
        callBack.setIndex(req.getIndex());
        session.sendMessage(callBack);
    }

    public abstract int cmdType();

    public static CallbackHandler queryHandler(int type) {
        return handlers.get(type);
    }

}
