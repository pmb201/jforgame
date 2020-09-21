package com.kingston.jforgame.socket.netty;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.message.IMessageDispatcher;
import com.kingston.jforgame.socket.message.Message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class IoEventHandler extends ChannelInboundHandlerAdapter {
	
	private final static Logger logger = LoggerFactory.getLogger(IoEventHandler.class);
	
	/** 消息分发器 */
	private IMessageDispatcher messageDispatcher;

	public IoEventHandler(IMessageDispatcher messageDispatcher) {
		super();
		this.messageDispatcher = messageDispatcher;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		if (!ChannelUtils.addChannelSession(ctx.channel(), new NettySession(channel))) {
			ctx.channel().close();
			logger.error("Duplicate session,IP=[{}]", ChannelUtils.getIp(channel));
		}
		logger.info("客户端连接上来了： {}", ChannelUtils.getIp(channel));
		IdSession userSession = ChannelUtils.getSessionBy(channel);
		messageDispatcher.onSessionCreated(userSession);
	}

	@Override
	public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
		Message packet = (Message) msg;
		logger.info("receive pact, content is {}", packet.getClass().getSimpleName());

		final Channel channel = context.channel();
		IdSession session = ChannelUtils.getSessionBy(channel);
		messageDispatcher.dispatch(session, packet);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		logger.info("客户端断开了连接： {}", ChannelUtils.getIp(channel));
		IdSession userSession = ChannelUtils.getSessionBy(channel);
		messageDispatcher.onSessionClosed(userSession);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel channel = ctx.channel();
		if (channel.isActive() || channel.isOpen()) {
			ctx.close();
		}
		if (!(cause instanceof IOException)) {
			logger.error("remote:" + channel.remoteAddress(), cause);
		}
	}
}
