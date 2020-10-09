package com.kingston.jforgame.socket.codec.netty;

import com.kingston.jforgame.socket.codec.IMessageEncoder;
import com.kingston.jforgame.socket.codec.SerializerHelper;
import com.kingston.jforgame.socket.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyProtocolEncoder extends MessageToByteEncoder<Message> {

	private Logger logger = LoggerFactory.getLogger(NettyProtocolEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
		// ----------------消息协议格式-------------------------
		// packetLength | moduleId | cmd | body
		// short           short     byte   byte[]
		// 其中 packetLength长度占2位，由编码链 LengthFieldPrepender(2) 提供

		short module = message.getModule();
		byte cmd = message.getCmd();

		try {
			// 消息元信息常量3表示消息body前面的两个字段，一个short表示module，一个byte表示cmd,
			final int metaSize = 3 + 8 + 13 + 32;
			IMessageEncoder msgEncoder = SerializerHelper.getInstance().getEncoder();
			byte[] body = msgEncoder.writeMessageBody(message);
			StringBuffer sb = new StringBuffer();
			//消息内容长度
			out.writeInt(body.length + metaSize);
			// 写入module类型
			out.writeShort(module);
			// 写入cmd类型
			out.writeByte(cmd);
			//写入版本
			String version = "01.00.01";
			//out.writeCharSequence(version.subSequence(0,version.length()), CharsetUtil.UTF_8);
			out.writeBytes(version.getBytes());
			//写入时间
			String dateTime = String.valueOf(System.currentTimeMillis());
			//out.writeCharSequence(dateTime.subSequence(0,dateTime.length()), CharsetUtil.UTF_8);
			out.writeBytes(dateTime.getBytes());
			//写入签名
			String signature = DigestUtils.md5Hex(sb.append(module).append(cmd).append(version).append(dateTime).toString());
			out.writeBytes(signature.getBytes());
			//out.writeCharSequence(signature.subSequence(0,signature.length()), CharsetUtil.UTF_8);
			//写入消息体
			out.writeBytes(body);
			//标记交给下一个编码器处理
			out.retain();
		} catch (Exception e) {
			logger.error("读取消息出错,模块号{}，类型{},异常{}", new Object[] { module, cmd, e });
		}
	}



}
