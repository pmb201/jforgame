package com.kingston.jforgame.server.net.netty;


import com.kingston.jforgame.server.ServerConfig;
import com.kingston.jforgame.server.net.MessageDispatcher;

import com.kingston.jforgame.socket.ServerNode;


import com.kingston.jforgame.socket.codec.netty.NettyProtocolDecoder;
import com.kingston.jforgame.socket.codec.netty.NettyProtocolEncoder;

import com.kingston.jforgame.socket.netty.IoEventHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.InetSocketAddress;
import java.util.List;


/**
 * @Author puMengBin
 * @Date 2020-09-08 20:46
 * @Description
 */
public class NettyWebSocketServer implements ServerNode {

    private Logger logger = LoggerFactory.getLogger(NettySocketServer.class);

    // 避免使用默认线程数参数
    private EventLoopGroup bossGroup = new NioEventLoopGroup(4);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private MessageDispatcher messageDispatcher ;

    private int maxReceiveBytes;

    public NettyWebSocketServer(int maxReceiveBytes,MessageDispatcher messageDispatcher) {
        this.maxReceiveBytes = maxReceiveBytes;
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void start() throws Exception {
        int serverPort = ServerConfig.getInstance().getWebSocketServerPort();
        logger.info("netty socket服务已启动，正在监听用户的请求@port:" + serverPort + "......");
        try {

            // 创建ServerBootstrap对象，它是Netty用于启动NIO服务端的辅助启动类， 目的是降低服务端的开发复杂度
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChildChannelHandler());

            // 处理业务
            bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));

            // 绑定端口，同步等待成功
            ChannelFuture f = bootstrap.bind(new InetSocketAddress(serverPort));
            //f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel arg0) throws Exception {
            ChannelPipeline pipeline = arg0.pipeline();

            /** HTTP请求的解码和编码 （以下handler为有序的，请不要随意更改其顺序）*/
            pipeline.addLast(new HttpServerCodec());

            // 把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse，
            // 原因是HTTP解码器会在每个HTTP消息中生成多个消息对象HttpRequest/HttpResponse,HttpContent,LastHttpContent
            pipeline.addLast(new HttpObjectAggregator(65536));

            pipeline.addLast("WebSocketAggregator", new WebSocketFrameAggregator(65536));

            // 主要用于处理大数据流，比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的; 增加之后就不用考虑这个问题了
            pipeline.addLast(new ChunkedWriteHandler());

            // WebSocket数据压缩
            pipeline.addLast(new WebSocketServerCompressionHandler());

            // 协议包长度限制
            pipeline.addLast(new WebSocketServerProtocolHandler("/", null, true));

            // 客户端300秒没收发包，便会触发UserEventTriggered事件到IdleEventHandler
            pipeline.addLast(new IdleStateHandler(0, 0, 300));

            /** 协议包解码 */
            pipeline.addLast(new MessageToMessageDecoder<WebSocketFrame>() {
                @Override
                protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> objs) throws Exception {
                    ByteBuf buf = ((BinaryWebSocketFrame)frame).content();
                    objs.add(buf);
                    buf.retain();
                }
            });

            /** 原框架解码解码 */
            pipeline.addLast(new NettyProtocolDecoder(maxReceiveBytes));

            //事件分发
            pipeline.addLast(new IoEventHandler(messageDispatcher));

            /** 协议包编码 */
            pipeline.addLast(new MessageToMessageEncoder<ByteBuf>() {
                @Override
                protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
                    BinaryWebSocketFrame data = new BinaryWebSocketFrame(msg);
                    out.add(data);
                }
            });
            /** 原框架解码编码 */
            pipeline.addLast(new NettyProtocolEncoder());


        }
    }

}
