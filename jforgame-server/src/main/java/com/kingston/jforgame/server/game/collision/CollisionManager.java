package com.kingston.jforgame.server.game.collision;

import com.kingston.jforgame.common.utils.AssertUtil;
import com.kingston.jforgame.common.utils.BlockingUniqueQueue;
import com.kingston.jforgame.orm.OrmBridge;
import com.kingston.jforgame.orm.OrmProcessor;
import com.kingston.jforgame.orm.SqlFactory;
import com.kingston.jforgame.orm.utils.DbHelper;
import com.kingston.jforgame.server.db.DbService;
import com.kingston.jforgame.server.db.DbUtils;
import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.game.collision.error.CollisionError;
import com.kingston.jforgame.server.game.collision.message.req.ReqAddScore;
import com.kingston.jforgame.server.game.collision.message.req.ReqUserOption;
import com.kingston.jforgame.server.game.collision.message.res.*;
import com.kingston.jforgame.server.game.collision.model.UserOption;
import com.kingston.jforgame.server.game.room.RoomManager;
import com.kingston.jforgame.server.game.room.model.RoomProfile;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.session.SessionManager;

import java.sql.SQLException;
import java.util.*;

/**
 * @Author puMengBin
 * @Date 2020-09-27 17:39
 * @Description
 */
public class CollisionManager {

    public static final String appId = "1234567890";

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
        session.sendPacket(resJoinRoom);
        //todo 需要广播

        //todo 需要检查房间人数，如果任务满游戏配置人数。开始游戏

    }

    public void leaveRoom(IdSession session){
        AccountProfile accountProfile = checkLogin(session);
        GameContext.getRoomManager().leaveRoom(appId,accountProfile);
        ResLeaveRoom resLeaveRoom = new ResLeaveRoom();
        session.sendPacket(resLeaveRoom);
        //todo 需要广播
        //todo 需要检查房间任务。当游戏任务减为 0 时。结束游戏，销毁房间
    }

    public void userOption(IdSession session, ReqUserOption userOption){
        AccountProfile accountProfile = checkLogin(session);
        AssertUtil.assertTrue(accountProfile.isJoinRoom(), CollisionError.USER_NOT_LOGIN);

        RoomProfile roomProfile = accountProfile.getRoomProfile();
        AssertUtil.assertTrue(roomProfile.getAppId().equals(appId),CollisionError.USER_NOT_LOGIN);

        accountProfile.getUserOptions().add(userOption.build(accountProfile.getId()));
        ResUserOption resUserOption = new ResUserOption();
        session.sendPacket(resUserOption);
    }

    public void userScore(IdSession session, ReqAddScore reqAddScore){
        AccountProfile accountProfile = checkLogin(session);
        AssertUtil.assertTrue(accountProfile.isJoinRoom(), CollisionError.USER_NOT_LOGIN);

        RoomProfile roomProfile = accountProfile.getRoomProfile();
        AssertUtil.assertTrue(roomProfile.getAppId().equals(appId),CollisionError.USER_NOT_LOGIN);

        accountProfile.setScore(accountProfile.getScore() + reqAddScore.getScore());
        ResAddScore resAddScore = new ResAddScore();
        session.sendPacket(resAddScore);
        //todo 需要广播
    }

    public void startGame(long roomId){
        //修改房间状态
        RoomProfile roomProfile = GameContext.getRoomManager().updateRoomStatus(roomId,RoomProfile.Status.CLOSE.getCode());
        roomProfile.setUserOptionList(new LinkedList<>());
        //todo 广播初始数据

        //todo 启动帧同步任务

    }

    public void endGame(long roomId) throws SQLException {

        //存储房间信息
        DbService.getInstance().insertOrUpdate(GameContext.getRoomManager().getByRoomId(roomId));
        //todo 销毁房间
        GameContext.getRoomManager().removeRoom(roomId);

        //todo 取消帧同步任务


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

    private void verticalSynchronization(long roomId){
        RoomProfile roomProfile = GameContext.getRoomManager().getByRoomId(roomId);
        if(roomProfile != null && !roomProfile.isOpen()){
            List<PlayerOptions> userOptionList = new LinkedList<>();

            Set<AccountProfile> accountProfileSet = roomProfile.getAccounts();
            for (int i = 0; i < accountProfileSet.size() ; i++) {
                AccountProfile accountProfile = (AccountProfile) accountProfileSet.toArray()[i];
                PlayerOptions playerOptions = new PlayerOptions();
                playerOptions.setPlayerIndex(i);
                BlockingUniqueQueue queue = accountProfile.getUserOptions();
                List<byte[]> options = new LinkedList<>();
                for (int a = 0; a < 10; a++) {
                    UserOption userOption = (UserOption) queue.take();
                    if(userOption != null){
                        options.add(userOption.getOptionData());
                    }else if(a == 0){
                        options.add(accountProfile.getLastUserOption().getOptionData());
                        userOption = accountProfile.getLastUserOption();
                    }else{
                        userOption = new UserOption();
                        userOption.setOptionData(options.get(i-1));
                        options.add(options.get(i-1));
                    }
                    roomProfile.getUserOptionList().add(userOption);
                }
                playerOptions.setOptions(options);
            }
            ResUserOptions resUserOptions = new ResUserOptions();
            resUserOptions.setFrameSeq(roomProfile.getFrameSeq().getAndIncrement());
            resUserOptions.setPlayerOptions(userOptionList);
            GameContext.getRoomManager().sendBroadcast(roomId, RoomManager.SYSTEM_ID,resUserOptions);
        }
    }


}
