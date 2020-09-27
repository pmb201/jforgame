package com.kingston.jforgame.server.game.accout.entity;

import com.kingston.jforgame.server.db.BaseEntity;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.utils.IdGenerator;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@ToString
public class Account extends BaseEntity  {
	
	@Id
	@Column
	private long id;

	@Column(unique = true)
	private String unionId;
	
	@Column
	private String name;

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

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getUnionId() {
		return unionId;
	}

	public AccountProfile buildProfile(){
		AccountProfile accountProfile = new AccountProfile();
		accountProfile.setId(this.id);
		accountProfile.setName(this.name);
		accountProfile.setUnionId(this.unionId);
		return accountProfile;
	}


}
