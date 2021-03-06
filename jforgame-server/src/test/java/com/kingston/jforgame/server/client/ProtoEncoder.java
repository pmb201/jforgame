package com.kingston.jforgame.server.client;

import com.kingston.jforgame.socket.codec.IMessageEncoder;
import com.kingston.jforgame.socket.codec.SerializerHelper;
import com.kingston.jforgame.socket.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.AttributeKey;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author puMengBin
 * @Date 2020-09-15 17:47
 * @Description
 */
public class ProtoEncoder extends ChannelOutboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ProtoEncoder.class);
    public static final AttributeKey<Short> SEND_SID = AttributeKey.valueOf("SEND_SID");


    public ProtoEncoder() {

    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Message) {
            Message packet = (Message) msg;
            IMessageEncoder msgEncoder = SerializerHelper.getInstance().getEncoder();
            byte[] data = msgEncoder.writeMessageBody(packet);


            String version = "01.00.01";
            String date = System.currentTimeMillis() + "";



            ByteBuf buf = ctx.alloc().buffer(data.length + 7 );//23 + signature.getBytes(CharsetUtil.UTF_8).length);
            buf.writeInt(data.length + 3 + 8 + 13 + 32); //+ 19 + signature.getBytes(CharsetUtil.UTF_8).length);
            buf.writeShort(packet.getModule());
            buf.writeByte(packet.getCmd());
            buf.writeBytes(version.getBytes());
            buf.writeBytes(date.getBytes());

            StringBuffer sb = new StringBuffer();
            sb.append(packet.getModule()).append(packet.getCmd()).append(version).append(date);

            String signature = DigestUtils.md5Hex(sb.toString());
            buf.writeBytes(signature.getBytes());

            buf.writeBytes(data);
            msg = new BinaryWebSocketFrame(buf);

        }
        super.write(ctx, msg, promise);
    }

}
