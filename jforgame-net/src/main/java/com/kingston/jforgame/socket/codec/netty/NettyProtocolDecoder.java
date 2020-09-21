package com.kingston.jforgame.socket.codec.netty;

import java.util.List;

import com.kingston.jforgame.socket.codec.IMessageDecoder;
import com.kingston.jforgame.socket.codec.SerializerHelper;
import com.kingston.jforgame.socket.combine.CombineMessage;
import com.kingston.jforgame.socket.combine.Packet;
import com.kingston.jforgame.socket.message.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyProtocolDecoder extends ByteToMessageDecoder {

    private int maxReceiveBytes;

    private Logger logger = LoggerFactory.getLogger(NettyProtocolDecoder.class);

    public NettyProtocolDecoder(int maxReceiveBytes) {
        this.maxReceiveBytes = maxReceiveBytes;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
        if (in.readableBytes() < 4) {
            return;
        }
        IMessageDecoder msgDecoder = SerializerHelper.getInstance().getDecoder();
        in.markReaderIndex();
        // ----------------消息协议格式-------------------------
        // packetLength | moduleId | cmd | version | datetime | signature | body
        // int short byte int long string byte[]
        int length = in.readInt();
        if (length > maxReceiveBytes) {
            logger.error("单包长度[{}]过大，断开链接", length);
            ctx.close();
            return;
        }

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        // 消息元信息常量3表示消息body前面的两个字段，一个short表示module，一个byte表示cmd,
        short moduleId = in.readShort();
        byte cmd = in.readByte();
        CharSequence versionSequence = in.readCharSequence(8,CharsetUtil.UTF_8);
        String version = String.valueOf(versionSequence);

        CharSequence dateSequence = in.readCharSequence(13,CharsetUtil.UTF_8);
        String datetime = String.valueOf(dateSequence);

        CharSequence signatureSequence = in.readCharSequence(32,CharsetUtil.UTF_8);
        String signature = String.valueOf(signatureSequence);

        StringBuffer sb = new StringBuffer();
        sb.append(moduleId).append(cmd).append(version).append(datetime);
        String buildSignature = DigestUtils.md5Hex(sb.toString());

        if(!buildSignature.equals(signature)){
            System.out.println("签名错误");
        }

        final int metaSize = 3 + 8 + 13 + 32;// + 4 + 8 + 4 + signatureLength ;
        byte[] body = new byte[length - metaSize];
        in.readBytes(body);
        Message msg = msgDecoder.readMessage(moduleId, cmd, body);

        if (moduleId > 0) {
            out.add(msg);
        } else { // 属于组合包
            CombineMessage combineMessage = (CombineMessage) msg;
            List<Packet> packets = combineMessage.getPackets();
            for (Packet packet : packets) {
                // 依次拆包反序列化为具体的Message
                out.add(Packet.asMessage(packet));
            }
        }
    }

}
