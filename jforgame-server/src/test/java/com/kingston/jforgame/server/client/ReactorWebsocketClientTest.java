package com.kingston.jforgame.server.client;



import com.kingston.jforgame.server.ServerScanPaths;
import com.kingston.jforgame.server.game.login.message.req.ReqAccountLogin;
import com.kingston.jforgame.socket.message.MessageFactory;


/**
 * Created by dongt
 * 2019-12-03 15:42
 */
public class ReactorWebsocketClientTest {


    public static void main(String[] args) {

        MessageFactory.INSTANCE.initMessagePool(ServerScanPaths.MESSAGE_PATH);
        NettWebcocketClient client = new NettWebcocketClient();
        client.connect("172.0.15.68",9528);

        ReqAccountLogin position = new ReqAccountLogin();
        position.setAccountId(123L);
        position.setPassword("kingston");
        client.send(position);
    }
}
