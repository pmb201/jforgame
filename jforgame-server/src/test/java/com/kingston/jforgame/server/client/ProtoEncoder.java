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
import io.netty.util.CharsetUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.provider.MD5;

import java.nio.ByteBuffer;

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

            String signature = "askjhdjksadhfkjjh";
            String version = "01.00.01";

            signature = DigestUtils.md5Hex("101101.00.011600508157704");

            ByteBuf buf = ctx.alloc().buffer(data.length + 7 );//23 + signature.getBytes(CharsetUtil.UTF_8).length);
            buf.writeInt(data.length + 3); //+ 19 + signature.getBytes(CharsetUtil.UTF_8).length);
            buf.writeShort(packet.getModule());
            buf.writeByte(packet.getCmd());
            //buf.writeInt(1);
            //buf.writeLong(System.currentTimeMillis());
            //buf.writeInt(signature.getBytes(CharsetUtil.UTF_8).length);
            //buf.writeCharSequence(signature.subSequence(0,signature.length()),CharsetUtil.UTF_8);
            buf.writeBytes(data);
            msg = new BinaryWebSocketFrame(buf);
            /*if (checkSum == null) {
                int size = 7 + packet.getBytes().length;
                ByteBuf buf = ctx.alloc().buffer(size);
                try {
                    buf.writeByte(packet.getHead());
                    buf.writeShort(packet.getBytes().length + 4);
                    buf.writeInt(packet.getCmd());
                    buf.writeBytes(packet.getBytes());
                    msg = new BinaryWebSocketFrame(buf);
                } catch (Exception e) {
                    buf.release();
                    throw e;
                }
            } else {
                int size = 7 + packet.getBytes().length + checkSum.length();
                ByteBuf buf = ctx.alloc().buffer(size);
                try {
                    buf.writeByte(packet.getHead());
                    size = 2 + 2 + 4 + packet.getBytes().length;
                    ByteBuf temp = Unpooled.buffer(size, size);
                    temp.writeShort(getSid(ctx));
                    temp.writeShort(packet.getBytes().length + 4);
                    temp.writeInt(packet.getCmd());
                    temp.writeBytes(packet.getBytes());
                    byte[] check = checkSum.checksum(temp.array());
                    buf.writeBytes(check);
                    buf.writeBytes(temp);
                    temp.release();
                    msg = new BinaryWebSocketFrame(buf);
                } catch (Exception e) {
                    buf.release();
                    throw e;
                }
            }*/
        }
        super.write(ctx, msg, promise);
    }


    public static void main(String[] args) {
        String signature = DigestUtils.md5Hex("101101.00.011600508157704");
        System.out.println(signature.equals("4a23f3ad0f0e472b0624bca9869a195a"));
    }

}
