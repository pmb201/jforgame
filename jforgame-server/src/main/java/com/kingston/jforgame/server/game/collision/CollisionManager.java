package com.kingston.jforgame.server.game.collision;

import com.kingston.jforgame.common.utils.AssertUtil;
import com.kingston.jforgame.common.utils.BlockingUniqueQueue;
import com.kingston.jforgame.common.utils.SchedulerManager;
import com.kingston.jforgame.server.db.DbService;
import com.kingston.jforgame.server.game.GameContext;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.game.collision.error.CollisionError;
import com.kingston.jforgame.server.game.collision.message.req.ReqAddScore;
import com.kingston.jforgame.server.game.collision.message.req.ReqCollision;
import com.kingston.jforgame.server.game.collision.message.req.ReqUserPosition;
import com.kingston.jforgame.server.game.collision.message.req.ReqUserTMove;
import com.kingston.jforgame.server.game.collision.message.res.*;
import com.kingston.jforgame.server.game.play.record.entity.PlayRecord;
import com.kingston.jforgame.server.game.room.RoomManager;
import com.kingston.jforgame.server.game.room.model.RoomProfile;
import com.kingston.jforgame.server.logs.LoggerUtils;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.session.SessionManager;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author puMengBin
 * @Date 2020-09-27 17:39
 * @Description
 */
public class CollisionManager {

    public static final String appId = "1234567890";

    private static final float[][] positions = {{-40,0,-40},{-30,0,-30},{-20,0,-20},{-10,0,-10},{40,0,40},{30,0,30},{20,0,20},{10,0,10},{0,0,0}};

    public static final List<AccountId> accountIds = new ArrayList<>();

    static {
        for(int i = 1; i < 100; i++){
            accountIds.add(new AccountId(i * 100 ,false));
        }
    }

    public void createRoom(IdSession session){
        AccountProfile accountProfile = checkLogin(session);
        RoomProfile roomProfile = GameContext.getRoomManager().createRoom(appId,3,accountProfile);
        ResCreateRoom resCreateRoom = new ResCreateRoom(roomProfile);
        session.sendPacket(resCreateRoom);
        //todo 需要添加定时开启游戏任务

        //todo 需要给房间设置游戏房间配置

    }

    public void joinRoom(IdSession session,long roomId){
        long accountId1 = SessionManager.INSTANCE.getAccountIdBy(session);
        AccountProfile accountProfile = GameContext.getAccountManager().getOnlineUser(accountId1);
        //AccountProfile accountProfile = checkLogin(session);
        if(accountProfile == null){
            accountProfile = new AccountProfile();
            accountProfile.setStatus(AccountProfile.Status.ON_LINE.getCode());
            for(AccountId accountId : accountIds){
                if(!accountId.used){
                    accountProfile.setId(accountId.getId());
                    accountId.setUsed(true);
                    break;
                }
            }
            SessionManager.INSTANCE.bindAccount(accountProfile.getId(),session);
            GameContext.getAccountManager().add2Online(accountProfile);
        }

        RoomProfile roomProfile = GameContext.getRoomManager().joinRoom(roomId,appId,3,accountProfile);
        if(roomProfile != null) {
            //todo 需要检查房间人数，如果任务满游戏配置人数。开始游戏
            if (roomProfile.isFull()) {
                GameContext.getRoomManager().updateRoomStatus(roomId, RoomProfile.Status.CLOSE.getCode());
                ResGameCanReady resGameCanReady = new ResGameCanReady();
                GameContext.getRoomManager().sendBroadcast(roomProfile.getId(), RoomManager.SYSTEM_ID, resGameCanReady);
            }
            ResJoinRoom resJoinRoom = new ResJoinRoom();
            resJoinRoom.setAccountId(accountProfile.getId());
            resJoinRoom.setIndex(roomProfile.getIndex());
            //resJoinRoom.setRoomProfile(roomProfile);

            System.err.println(accountProfile.getId() + "   " + roomProfile.getId());
            session.sendPacket(resJoinRoom);
            //todo 需要广播


        }
    }

    public void leaveRoom(IdSession session){
        AccountProfile accountProfile = checkLogin(session);


        GameContext.getRoomManager().leaveRoom(appId,accountProfile);
        ResLeaveRoom resLeaveRoom = new ResLeaveRoom();
        session.sendPacket(resLeaveRoom);
        //todo 需要广播
        //todo 需要检查房间任务。当游戏任务减为 0 时。结束游戏，销毁房间
    }

    /*public void userOption(IdSession session, ReqUserOption userOption){
        AccountProfile accountProfile = checkLogin(session);
        AssertUtil.assertTrue(accountProfile.isJoinRoom(), CollisionError.USER_NOT_IN_GAME);

        RoomProfile roomProfile = GameContext.getRoomManager().getByRoomId(accountProfile.getRoomId());
        AssertUtil.assertTrue(roomProfile.getAppId().equals(appId),CollisionError.USER_NOT_IN_GAME);

        accountProfile.getUserOptions().add(userOption.build(accountProfile.getId()));
        ResUserOption resUserOption = new ResUserOption();
        session.sendPacket(resUserOption);
    }*/

    public void userPosition(IdSession session, ReqUserPosition userPosition){
        AccountProfile accountProfile = checkLogin(session);
        AssertUtil.assertTrue(accountProfile.isJoinRoom(), CollisionError.USER_NOT_IN_GAME);

        RoomProfile roomProfile = GameContext.getRoomManager().getByRoomId(accountProfile.getRoomId());
        AssertUtil.assertTrue(roomProfile.getAppId().equals(appId),CollisionError.USER_NOT_IN_GAME);

        //accountProfile.getUserOptions().add(userOption.build(accountProfile.getId()));
        //ResUserOption resUserOption = new ResUserOption();
        //session.sendPacket(resUserOption);
        //广播位置信息
        ResPlayersPosition resPlayersPosition = new ResPlayersPosition();
        AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
        Integer index = null;
        for(int i = 0; i < accountProfiles.length; i++){
            AccountProfile accountProfile1 = accountProfiles[i];
            if(accountProfile1 != null && accountProfile.getId() == accountProfile1.getId()){
                index = i;
                break;
            }
        }
        resPlayersPosition.setIndex(index);
        resPlayersPosition.setAccountId(accountProfile.getId());
        resPlayersPosition.setPosition(userPosition.getPosition());
        resPlayersPosition.setSpeed(userPosition.getSpeed());
        resPlayersPosition.setDateTime(System.currentTimeMillis());
        roomProfile.getUserOptionList().add(resPlayersPosition);
        //roomProfile.getUserOptions().add(resPlayersPosition);
        GameContext.getRoomManager().sendBroadcast(accountProfile.getRoomId(),RoomManager.SYSTEM_ID,resPlayersPosition);
    }

    public void userScore(IdSession session, ReqAddScore reqAddScore){
        AccountProfile accountProfile = checkLogin(session);
        AssertUtil.assertTrue(accountProfile.isJoinRoom(), CollisionError.USER_NOT_IN_GAME);

        RoomProfile roomProfile = GameContext.getRoomManager().getByRoomId(accountProfile.getRoomId());
        AssertUtil.assertTrue(roomProfile.getAppId().equals(appId),CollisionError.USER_NOT_IN_GAME);
        accountProfile.getScore().addAndGet(reqAddScore.getScore());

        //todo 需要广播
        broadcastUserScore(accountProfile.getRoomId());
    }

    public void ready(IdSession session){
        AccountProfile accountProfile = checkLogin(session);
        AssertUtil.assertTrue(accountProfile.isJoinRoom(), CollisionError.USER_NOT_IN_GAME);
        //AssertUtil.assertTrue(accountProfile.isGaming(),CollisionError.USER_GAMING);
        if(accountProfile.isGaming()){
            return;
        }
        accountProfile.setStatus(AccountProfile.Status.GAMING.getCode());
        RoomProfile roomProfile = GameContext.getRoomManager().getByRoomId(accountProfile.getRoomId());
        if(roomProfile.isAllReady()){
            startGame(accountProfile.getRoomId());
        }
    }

    public void startGame(long roomId){
        //修改房间状态
        RoomProfile roomProfile = GameContext.getRoomManager().updateRoomStatus(roomId,RoomProfile.Status.CLOSE.getCode());
        //广播初始数据
        roomProfile.setUserOptionList(new LinkedList<>());
        List<ResPlayersPosition> resPlayersPositions = new LinkedList<>();
        AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
        for (int i = 0; i < accountProfiles.length; i++) {
            AccountProfile accountProfile1 = accountProfiles[i];
            if(accountProfile1 != null){
                ResPlayersPosition position = new ResPlayersPosition();
                position.setSpeed(0);
                position.setPosition(new Position(positions[i][0],positions[i][1],positions[i][2]));
                position.setDateTime(System.currentTimeMillis());
                position.setAccountId(accountProfile1.getId());
                resPlayersPositions.add(position);
            }

        }
        roomProfile.getUserOptionList().addAll(resPlayersPositions);
        ResPlayersPositions resPlayersPositionsMessage = new ResPlayersPositions();
        resPlayersPositionsMessage.setPlayersPositions(resPlayersPositions);
        GameContext.getRoomManager().sendBroadcast(roomId,RoomManager.SYSTEM_ID,resPlayersPositionsMessage);
        //启动帧同步任务
        //SchedulerManager.INSTANCE.scheduleAtFixedRate("verticalSynchronization:" + roomProfile.getId(),
        //        () -> verticalSynchronization(roomProfile.getId()),50,50);
        //定时关闭房间
        //SchedulerManager.INSTANCE.registerUniqueTimeoutTask("endGame"+ roomProfile.getId(),() -> endGame(roomProfile.getId()),1000 * 60 * 2);
        //广播分数排行
        broadcastUserScore(roomId);
    }

    public void endGame(long roomId){
        try{
            //todo 存储对局信息，以及对局回放信息
            RoomProfile roomProfile = GameContext.getRoomManager().getByRoomId(roomId);
            PlayRecord record = roomProfile.buildPlayRecord();
            DbService.getInstance().insertOrUpdate(record);
            System.out.println(record);
            //DbService.getInstance().insertOrUpdate(GameContext.getRoomManager().getByRoomId(roomId));
        }catch (Exception e){
            e.printStackTrace();
        }
        //todo 需要广播游戏结束状态 并返回当前排名信息
        ResGameEnd resGameEnd = new ResGameEnd();
        resGameEnd.setAccountScoreList(buildAccountScores(roomId));
        GameContext.getRoomManager().sendBroadcast(roomId,RoomManager.SYSTEM_ID,resGameEnd);

        //todo 销毁房间
        GameContext.getRoomManager().destroyRoom(roomId);
        //todo 取消帧同步任务
        SchedulerManager.INSTANCE.cancleTask("verticalSynchronization:"+roomId);
    }

    public void userCollision(IdSession session, ReqCollision reqCollision){


        AccountProfile accountProfile = checkLogin(session);
        AssertUtil.assertTrue(accountProfile.isJoinRoom(), CollisionError.USER_NOT_IN_GAME);
        if(reqCollision.getAccountId() < 1){
            LoggerUtils.error("参数：{},{}",accountProfile.getId(),reqCollision.toString());
        }
        RoomProfile roomProfile = GameContext.getRoomManager().getByRoomId(accountProfile.getRoomId());
        AssertUtil.assertTrue(roomProfile.getAppId().equals(appId),CollisionError.USER_NOT_IN_GAME);
        AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
        Integer index = null,index1 = null;
        for(int i = 0; i < accountProfiles.length; i++){
            AccountProfile accountProfile1 = accountProfiles[i];
            if(accountProfile1 != null && reqCollision.getAccountId() == accountProfile1.getId()){
                index = i;
            }

        }
        ResCollision resCollision = new ResCollision();
        resCollision.setIndex(index);
        resCollision.setAccountId(reqCollision.getAccountId());
        resCollision.setSpeed(reqCollision.getSpeed());
        resCollision.setSubSpeed(reqCollision.getSubSpeed());
        resCollision.setAngle(reqCollision.getAngle());
        GameContext.getRoomManager().sendBroadcast(accountProfile.getRoomId(),accountProfile.getId(),resCollision);
    }

    public void userToMove(IdSession session, ReqUserTMove reqUserTMove){
        AccountProfile accountProfile = checkLogin(session);
        AssertUtil.assertTrue(accountProfile.isJoinRoom(), CollisionError.USER_NOT_IN_GAME);

        RoomProfile roomProfile = GameContext.getRoomManager().getByRoomId(accountProfile.getRoomId());
        AssertUtil.assertTrue(roomProfile.getAppId().equals(appId),CollisionError.USER_NOT_IN_GAME);
        AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
        Integer index = null;
        for(int i = 0; i < accountProfiles.length; i++){
            AccountProfile accountProfile1 = accountProfiles[i];
            if(accountProfile1 != null && accountProfile.getId() == accountProfile1.getId()){
                index = i;
                break;
            }
        }
        ResUserTMove resUserTMove = new ResUserTMove();
        resUserTMove.setIndex(index);
        resUserTMove.setAccountId(accountProfile.getId());
        resUserTMove.setHitPos(reqUserTMove.getHitPos());
        resUserTMove.setSpeed(reqUserTMove.getSpeed());
        resUserTMove.setSubSpeed(reqUserTMove.getSubSpeed());
        resUserTMove.setAngle(reqUserTMove.getAngle());
        GameContext.getRoomManager().sendBroadcast(accountProfile.getRoomId(),RoomManager.SYSTEM_ID,resUserTMove);
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

    /**
     * @Description: 帧同步
     * @Author: PuMengBin
     * @Date: 2020-09-28 19:25
     * @param roomId:
     void
     **/
    private void verticalSynchronization(long roomId){
        RoomProfile roomProfile = GameContext.getRoomManager().getByRoomId(roomId);
        if(roomProfile != null && !roomProfile.isOpen()){
            List<ResPlayersPosition> userAllPositions = new LinkedList<>();
            List<ResPlayersPosition> userLastPositions = new LinkedList<>();
            BlockingUniqueQueue queue = roomProfile.getUserOptions();
            int size = queue.size();
            if(size > 0){
                for (int i = 0; i < size; i++) {
                    userAllPositions.add((ResPlayersPosition) queue.take());
                }
                AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
                for(AccountProfile accountProfile : accountProfiles){
                    Optional<ResPlayersPosition> lastPosition = userAllPositions.stream()
                            .filter(resPlayersPosition -> resPlayersPosition.getAccountId() == accountProfile.getId())
                            .sorted(Comparator.comparing(ResPlayersPosition::getDateTime).reversed())
                            .findFirst();
                    if(lastPosition.isPresent()){
                        userLastPositions.add(lastPosition.get());
                    }

                }


                ResPlayersMovePositions resPlayersPositions = new ResPlayersMovePositions();
                resPlayersPositions.setPlayersPositions(userLastPositions);
                GameContext.getRoomManager().sendBroadcast(roomId, RoomManager.SYSTEM_ID,resPlayersPositions);
            }

            /*AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
            for (int i = 0; i < accountProfiles.length ; i++) {
                AccountProfile accountProfile = accountProfiles[i];
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
                    //roomProfile.getUserOptionList().add(userOption);
                }
                playerOptions.setOptions(options);
            }*/

            /*ResUserOptions resUserOptions = new ResUserOptions();
            resUserOptions.setFrameSeq(roomProfile.getFrameSeq().getAndIncrement());
            resUserOptions.setPlayerOptions(userOptionList);*/

        }
    }

    private void broadcastUserScore(long roomId){
        RoomProfile roomProfile = GameContext.getRoomManager().getByRoomId(roomId);
        if(roomProfile != null && !roomProfile.isOpen()){
            ResAddScore resAddScore = new ResAddScore();
            List<AccountScore> accountScores = buildAccountScores(roomId);
            resAddScore.setAccountScoreList(accountScores);
            GameContext.getRoomManager().sendBroadcast(roomId,RoomManager.SYSTEM_ID,resAddScore);
        }
    }

    private List<AccountScore> buildAccountScores(long roomId){
        RoomProfile roomProfile = GameContext.getRoomManager().getByRoomId(roomId);
        List<AccountScore> accountScores = new ArrayList<>();
        AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
        for (int i = 0; i < accountProfiles.length; i++) {
            AccountProfile accountProfile = accountProfiles[i];
            if(accountProfile != null){
                AccountScore accountScore = new AccountScore();
                accountScore.setIndex(i);
                accountScore.setAccountId(accountProfile.getId());
                accountScore.setScore(accountProfile.getScore().get());
                accountScores.add(accountScore);
            }
        }
        accountScores = accountScores.stream().sorted(Comparator.comparing(AccountScore::getScore).reversed()).collect(Collectors.toList());
        return accountScores;
    }

    @AllArgsConstructor
    @Data
    public static class AccountId{


        private long id;

        private boolean used;
    }
}
