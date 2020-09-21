package com.kingston.jforgame.server.game.cross.ladder.message;

import com.kingston.jforgame.server.cross.core.match.AbstractMatchMessage;

public class Req_G2M_LadderApply extends AbstractMatchMessage {

	private long playerId;
	/** 积分 */
	private int score;
	/** 战力　*/
	private int power;

	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}

	@Override
	public String toString() {
		return "MReqLadderApplyMessage [playerId=" + playerId +
				", score=" + score + ", power=" + power + "]";
	}

}
