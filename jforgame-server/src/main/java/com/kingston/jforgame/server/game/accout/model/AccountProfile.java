package com.kingston.jforgame.server.game.accout.model;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@ToString
@ProtobufClass
public class AccountProfile {

	private Long id;

	private String unionId;

	private String momoId;

	private long roomId;

	private String name;

	/** 1,在线；0，离线 **/
	private int status;

	public boolean isJoinRoom() {
		return this.roomId > 0;
	}

	public static enum Status{
		ON_LINE(1),
		OFF_LINE(0);

		@Getter
		private int code;

		private Status(int code){
			this.code = code;
		}
	}
}
