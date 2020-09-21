package com.kingston.jforgame.socket.netty;

import java.util.HashMap;
import java.util.Map;

import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.message.Message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class NettySession implements IdSession {
	
	/** 网络连接channel */
	private Channel channel;
	
	/** 拓展用，保存一些个人数据  */
	private Map<String, Object> attrs = new HashMap<>();

	public NettySession(Channel channel) {
		super();
		this.channel = channel;
	}

	@Override
	public void sendPacket(Message packet) {
		ChannelFuture cf = channel.writeAndFlush(packet);
		//添加ChannelFutureListener以便在写操作完成后接收通知
		cf.addListener((ChannelFutureListener) future -> {
			//写操作完成，并没有错误发生
			if (future.isSuccess()){
				System.out.println("write successful");
			}else{
				//记录错误
				System.out.println("write error");
				future.cause().printStackTrace();
			}
		});
	}

	@Override
	public long getOwnerId() {
		if (attrs.containsKey(ID)) {
			return (long) attrs.get(ID);
		}
		return 0;
	}

	@Override
	public Object setAttribute(String key, Object value) {
		attrs.put(key.toString(), value);
		return value;
	}

	@Override
	public Object getAttribute(String key) {
		return attrs.get(key.toString());
	}

}
