package com.kingston.jforgame.socket.message;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.kingston.jforgame.socket.annotation.MessageMeta;

/**
 * base class of IO message
 */
public abstract class Message {

	/**
	 * messageMeta, module of message
	 * @return
	 */
	public short getModule() {
		MessageMeta annotation = getClass().getAnnotation(MessageMeta.class);
		if (annotation != null) {
			return annotation.module();
		}
		return 0;
	}

	/**
	 * messageMeta, subType of module
	 * @return
	 */
	public byte getCmd() {
		MessageMeta annotation = getClass().getAnnotation(MessageMeta.class);
		if (annotation != null) {
			return annotation.cmd();
		}
		return 0;
	}

	public String key() {
		return this.getModule() + "_" + this.getCmd();
	}

}
