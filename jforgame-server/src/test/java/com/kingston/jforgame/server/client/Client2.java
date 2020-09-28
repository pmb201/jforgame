package com.kingston.jforgame.server.client;

import com.kingston.jforgame.server.ServerScanPaths;
import com.kingston.jforgame.server.game.collision.message.req.ReqJoinRoom;
import com.kingston.jforgame.server.game.login.message.req.ReqAccountLogin;
import com.kingston.jforgame.socket.message.MessageFactory;

/**
 * @Author puMengBin
 * @Date 2020-09-27 20:03
 * @Description
 */
public class Client2 {

    public static void main(String[] args) {
        MessageFactory.INSTANCE.initMessagePool(ServerScanPaths.MESSAGE_PATH);
        NettWebcocketClient client = new NettWebcocketClient();
        client.connect("172.0.15.68",9528);

        ReqAccountLogin position = new ReqAccountLogin();
        position.setAccountId(104617);
        position.setPassword("kingston");
        position.setUnionId("987654321");
        client.send(position);

        ReqJoinRoom reqJoinRoom = new ReqJoinRoom();
        client.send(reqJoinRoom);
    }
}
