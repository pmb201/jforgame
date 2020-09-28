package com.kingston.jforgame.server.game.collision.entity;

import com.kingston.jforgame.server.db.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author puMengBin
 * @Date 2020-09-27 20:32
 * @Description
 */
@Entity
@Data
public class UserOptionRecord extends BaseEntity {

    @Id
    @Column
    private long id;

    @Column
    private long accountId;

    @Column
    private long dateTime;

    @Column
    private long gameRecordId;

    @Column
    private byte[] optionData;

    @Override
    public long getId() {
        return 0;
    }
}
