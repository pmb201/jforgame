package com.kingston.jforgame.server.game.accout.model;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.kingston.jforgame.server.game.collision.model.UserOption;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@ToString
public class AccountProfile {

	@Protobuf
	private Long id;

	@Protobuf
	private String unionId;

	@Protobuf
	private String momoId;

	@Protobuf
	private long roomId;

	@Protobuf
	private String name;

	/** 1,在线；0，离线 ;2,游戏中**/
	@Protobuf
	private int status;

	private AtomicInteger score = new AtomicInteger(0);

	private UserOption lastUserOption;

	public boolean isJoinRoom() {
		return this.roomId > 0;
	}

	public boolean isGaming() {
		return this.roomId > 0 && this.status == Status.GAMING.getCode();
	}

	public int getIntScore(){
		return score.intValue();
	}

	public static enum Status{
		/** 在线 */
		ON_LINE(1),
		/** lixian */
		OFF_LINE(0),
		/** 游戏中 */
		GAMING(2);

		@Getter
		private int code;

		private Status(int code){
			this.code = code;
		}
	}
}
