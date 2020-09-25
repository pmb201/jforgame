package com.kingston.jforgame.server.game.accout.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.kingston.jforgame.server.db.BaseEntity;
import com.kingston.jforgame.server.utils.IdGenerator;


@Entity
public class Account extends BaseEntity  {
	
	@Id
	@Column
	private long id;
	
	@Column
	private String name;

	private long roomId;
	
	public Account() {
		this.id = IdGenerator.getNextId();
	}

	@Override
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(long id) {
		this.id = id;
	}


	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public boolean isJoinRoom() {
		return this.roomId > 0;
	}
}
