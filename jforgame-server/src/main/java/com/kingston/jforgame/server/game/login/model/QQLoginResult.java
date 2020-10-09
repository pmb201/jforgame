package com.kingston.jforgame.server.game.login.model;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by dongt
 * 2020-05-18 10:24
 */
@Data
public class QQLoginResult{

    private String openid;
    @JsonProperty("unionid")
    private String unionId;
    @JsonProperty("errcode")
    private int errCode;
    @JsonProperty("errmsg")
    private String errMsg;
    @JsonProperty("session_key")
    private String sessionKey;
}
