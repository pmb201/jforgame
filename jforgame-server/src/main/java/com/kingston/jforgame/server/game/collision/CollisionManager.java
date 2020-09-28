package com.kingston.jforgame.server.game.collision;

import com.kingston.jforgame.common.utils.AssertUtil;
import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.game.collision.error.CollisionError;
import com.kingston.jforgame.server.game.collision.message.req.ReqAddScore;
import com.kingston.jforgame.server.game.collision.message.req.ReqUserOption;
import com.kingston.jforgame.server.game.collision.message.res.*;
import com.kingston.jforgame.server.game.room.model.RoomProfile;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.session.SessionManager;

/**
 * @Author puMengBin
 * @Date 2020-09-27 17:39
 * @Description
 */
public class CollisionManager {

    private static final String appId = "1234567890";

    public void createRoom(IdSession session){
        AccountProfile accountProfile = checkLogin(session);
        RoomProfile roomProfile = GameContext.getRoomManager().createRoom(appId,accountProfile);
        ResCreateRoom resCreateRoom = new ResCreateRoom(roomProfile);
        session.sendPacket(resCreateRoom);
        //todo 需要添加定时开启游戏任务
        //todo 需要给房间设置游戏房间配置
    }

    public void joinRoom(IdSession session,long roomId){
        AccountProfile accountProfile = checkLogin(session);
        RoomProfile roomProfile = GameContext.getRoomManager().joinRoom(roomId,appId,accountProfile);
        ResJoinRoom resJoinRoom = new ResJoinRoom();
        resJoinRoom.setRoomProfile(roomProfile);
        //todo 需要广播

        //todo 需要检查房间人数，如果任务满游戏配置人数。开始游戏
        session.sendPacket(resJoinRoom);
    }

    public void leaveRoom(IdSession session){
        AccountProfile accountProfile = checkLogin(session);
        GameContext.getRoomManager().leaveRoom(appId,accountProfile);
        ResLeaveRoom resLeaveRoom = new ResLeaveRoom();
        //todo 需要广播
        //todo 需要检查房间任务。当游戏任务减为 0 时。结束游戏，销毁房间
        session.sendPacket(resLeaveRoom);
    }

    public void userOption(IdSession session, ReqUserOption userOption){
        AccountProfile accountProfile = checkLogin(session);
        AssertUtil.assertTrue(accountProfile.isJoinRoom(), CollisionError.USER_NOT_LOGIN);

        RoomProfile roomProfile = accountProfile.getRoomProfile();
        AssertUtil.assertTrue(roomProfile.getAppId().equals(appId),CollisionError.USER_NOT_LOGIN);

        accountProfile.getUserOption().add(userOption.build(accountProfile.getId()));
        ResUserOption resUserOption = new ResUserOption();
        session.sendPacket(resUserOption);
    }

    public void userScore(IdSession session, ReqAddScore reqAddScore){
        AccountProfile accountProfile = checkLogin(session);
        AssertUtil.assertTrue(accountProfile.isJoinRoom(), CollisionError.USER_NOT_LOGIN);

        RoomProfile roomProfile = accountProfile.getRoomProfile();
        AssertUtil.assertTrue(roomProfile.getAppId().equals(appId),CollisionError.USER_NOT_LOGIN);

        accountProfile.setScore(accountProfile.getScore() + reqAddScore.getScore());
        //todo 需要广播
        ResAddScore resAddScore = new ResAddScore();
        session.sendPacket(resAddScore);
    }



    private AccountProfile checkLogin(IdSession session){
        AccountProfile accountProfile = null;
        long accountId = SessionManager.INSTANCE.getAccountIdBy(session);
        if(accountId > 0){
            accountProfile = GameContext.getAccountManager().getOnlineUser(accountId);
        }
        AssertUtil.assertNotNull(accountProfile, CollisionError.USER_NOT_LOGIN);
        return accountProfile;
    }


}
