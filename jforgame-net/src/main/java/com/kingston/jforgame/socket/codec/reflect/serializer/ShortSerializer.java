package com.kingston.jforgame.socket.codec.reflect.serializer;

import java.nio.ByteBuffer;

import com.kingston.jforgame.socket.utils.ByteBuffUtil;

public class ShortSerializer extends Serializer {

	@Override
	public Short decode(ByteBuffer in, Class<?> type, Class<?> wrapper) {
		return ByteBuffUtil.readShort(in);
	}

	@Override
	public void encode(ByteBuffer out, Object value, Class<?> wrapper) {
		ByteBuffUtil.writeShort(out, (short)value);
	}

}
