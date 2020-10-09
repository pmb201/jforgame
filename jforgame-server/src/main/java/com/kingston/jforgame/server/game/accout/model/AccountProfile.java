package com.kingston.jforgame.server.game.accout.model;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.kingston.jforgame.common.utils.BlockingUniqueQueue;
import com.kingston.jforgame.server.game.collision.model.UserOption;
import com.kingston.jforgame.server.game.room.model.RoomProfile;
import lombok.*;

@Getter
@Setter
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

	/** 1,在线；0，离线 **/
	@Protobuf
	private int status;

	private RoomProfile roomProfile;

	private BlockingUniqueQueue<UserOption> userOptions = new BlockingUniqueQueue<>();

	private int score;

	private UserOption lastUserOption;

	public boolean isJoinRoom() {
		return this.roomId > 0;
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
