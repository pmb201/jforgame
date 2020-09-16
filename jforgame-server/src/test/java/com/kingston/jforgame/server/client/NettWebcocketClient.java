package com.kingston.jforgame.server.client;

import com.kingston.jforgame.server.ServerScanPaths;
import com.kingston.jforgame.socket.codec.IMessageEncoder;
import com.kingston.jforgame.socket.codec.SerializerHelper;
import com.kingston.jforgame.socket.codec.netty.NettyProtocolDecoder;
import com.kingston.jforgame.socket.codec.netty.NettyProtocolEncoder;
import com.kingston.jforgame.socket.message.Message;
import com.kingston.jforgame.socket.message.MessageFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * @Author puMengBin
 * @Date 2020-09-15 17:21
 * @Description
 */
public class NettWebcocketClient {

    //private final CRC16CheckSum checkSum = new CRC16CheckSum();

    private Channel channel;

    public void connect(String host, int port){
        // 初始化协议池
        EventLoopGroup client = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(client);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);

        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                //HTTP编解码器
                pipeline.addLast("http_codec", new HttpClientCodec());
                //HTTP消息聚合,使用FullHttpResponse和FullHttpRequest到ChannelPipeline中的下一个ChannelHandler，这就消除了断裂消息，保证了消息的完整。
                pipeline.addLast("http_aggregator", new HttpObjectAggregator(65536));
                /** 协议包解码 */
                pipeline.addLast(new MessageToMessageDecoder<WebSocketFrame>() {
                    @Override
                    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> objs) throws Exception {
                        ByteBuf buf = ((BinaryWebSocketFrame)frame).content();
                        objs.add(buf);
                        buf.retain();
                    }
                });

                pipeline.addLast("protobuf_decoder", new NettyProtocolDecoder(9999));
                pipeline.addLast("client_handler", new ClientHandler());
                pipeline.addLast("protobuf_encoder", new ProtoEncoder());
            }
        });

        ChannelFuture future;
        try {
            URI websocketURI = new URI(String.format("ws://%s:%d/", host, port));
            HttpHeaders httpHeaders = new DefaultHttpHeaders();
            //进行握手
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, (String)null, true,httpHeaders);
            channel = bootstrap.connect(websocketURI.getHost(), websocketURI.getPort()).sync().channel();
            ClientHandler handler = (ClientHandler)channel.pipeline().get("client_handler");
            handler.setHandshaker(handshaker);
            // 通过它构造握手响应消息返回给客户端，
            // 同时将WebSocket相关的编码和解码类动态添加到ChannelPipeline中，用于WebSocket消息的编解码，
            // 添加WebSocketEncoder和WebSocketDecoder之后，服务端就可以自动对WebSocket消息进行编解码了
            handshaker.handshake(channel);
            //阻塞等待是否握手成功
            future = handler.handshakeFuture().sync();
            System.out.println("----channel:"+future.channel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //future.channel().closeFuture().awaitUninterruptibly();
    }

    public void send(Message msg) {
        if (channel == null || msg == null || !channel.isWritable()) {
            return;
        }
//        int cmd = ProtoManager.getMessageID(msg);
//        Packet packet = new Packet(Packet.HEAD_TCP, cmd, msg.toByteArray());
        channel.writeAndFlush(msg);
    }
}
