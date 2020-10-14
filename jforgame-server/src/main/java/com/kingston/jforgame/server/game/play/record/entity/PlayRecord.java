package com.kingston.jforgame.server.game.play.record.entity;

import com.kingston.jforgame.server.db.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author puMengBin
 * @Date 2020-10-13 09:48
 * @Description
 */
@Entity
@Data
public class PlayRecord extends BaseEntity {


    @Id
    @Column
    private long id;

    @Column
    private String accounts;

    @Column
    private String appId;

    @Column
    private String createTime;



}