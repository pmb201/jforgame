package com.kingston.jforgame.server.game.room;



import com.kingston.jforgame.common.utils.AssertUtil;
import com.kingston.jforgame.common.utils.RandomUtil;
import com.kingston.jforgame.server.game.accout.model.AccountProfile;
import com.kingston.jforgame.server.game.room.error.RoomErrorCode;
import com.kingston.jforgame.server.game.room.model.RoomProfile;
import com.kingston.jforgame.server.logs.LoggerUtils;
import com.kingston.jforgame.socket.IdSession;
import com.kingston.jforgame.socket.message.Message;
import com.kingston.jforgame.socket.session.SessionManager;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @Author puMengBin
 * @Date 2020-09-24 11:22
 * @Description
 */
public class RoomManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    ReentrantLock lock = new ReentrantLock();

    public static int SYSTEM_ID = 0;

    private Map<String, Map<Long,RoomProfile>> appRoomMap = new ConcurrentHashMap<>();

    public RoomProfile createRoom(String appId, int maxPlayerNum,AccountProfile account){
        AssertUtil.assertNotNull(account, RoomErrorCode.ACCOUNT_NOT_EXIST);
        if(isJoinRoom(account)){
            return getByRoomId(account.getRoomId());
        }
        Map<Long,RoomProfile> roomProfiles = appRoomMap.get(appId);
        if(roomProfiles == null){
            roomProfiles = new ConcurrentHashMap<>();
        }

        RoomProfile roomProfile = new RoomProfile(appId,maxPlayerNum,account);
        roomProfiles.put(roomProfile.getId(),roomProfile);
        roomProfile.setIndex(0);
        appRoomMap.put(appId,roomProfiles);
        account.setRoomId(roomProfile.getId());
        //account.setRoomProfile(roomProfile);
        return roomProfile;
    }

    public boolean isJoinRoom(AccountProfile userInfo) {
        boolean userInRoom = false;
        for(Map.Entry<String,Map<Long,RoomProfile>> entry : appRoomMap.entrySet()){
            if(entry != null && entry.getValue().containsKey(userInfo.getRoomId())){
                userInRoom = true;
                break;
            }
        }
        return userInfo.isJoinRoom() && userInRoom;
    }

    public RoomProfile joinRoom(Long roomId,String appId,int maxPlayerNum,AccountProfile account){
        if(lock.tryLock()){
            try {
                if (isJoinRoom(account)) {
                    return getByRoomId(account.getRoomId());
                }
                RoomProfile roomProfile;
                if (roomId == null || roomId <= 0) {
                    Map<Long, RoomProfile> appRooms = appRoomMap.get(appId);
                    if (appRooms == null) {
                        roomProfile = createRoom(appId, maxPlayerNum,account);
                    } else {
                        List<RoomProfile> canJoinRooms = appRooms.entrySet().stream()
                                .filter(appRoomProfile -> appRoomProfile.getValue().canJoin())
                                .map(appRoomProfile -> appRoomProfile.getValue())
                                .collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(canJoinRooms)) {
                            roomProfile = createRoom(appId,maxPlayerNum,account);
                        } else {
                            roomProfile = canJoinRooms.get(RandomUtil.nextInt(canJoinRooms.size()));
                            roomProfile.getPlayerNum().incrementAndGet();
                            //Set<AccountProfile> accountSet = roomProfile.getAccounts();
                            //accountSet.add(account);
                            AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
                            for (int i = 0; i < accountProfiles.length; i++) {
                                if(accountProfiles[i] == null){
                                    accountProfiles[i] = account;
                                    roomProfile.setIndex(i);
                                    break;
                                }
                            }
                        }
                    }
                    account.setRoomId(roomProfile.getId());
                    //account.setRoomProfile(roomProfile);
                } else {
                    roomProfile = getByRoomId(roomId);
                    AssertUtil.assertNotNull(roomProfile, RoomErrorCode.ROOM_NOT_EXIST);
                    AssertUtil.assertTrue(roomProfile.getRoomStatus() == RoomProfile.Status.CREATE.getCode(), RoomErrorCode.ROOM_CAN_NOT_JOIN);
                    AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
                    for (int i = 0; i < accountProfiles.length; i++) {
                        if(accountProfiles[i] == null){
                            accountProfiles[i] = account;
                            roomProfile.setIndex(i);
                            break;
                        }
                    }
                    account.setRoomId(roomProfile.getId());
                    //account.setRoomProfile(roomProfile);
                }
                return roomProfile;
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
        return null;
    }


    public RoomProfile leaveRoom(String appId,AccountProfile account){
        RoomProfile roomProfile = null;
        try {
            if(lock.tryLock()){
                if(account.isJoinRoom()){
                    roomProfile = getByRoomId(account.getRoomId());
                    if(roomProfile.getRoomStatus() == RoomProfile.Status.CREATE.getCode()){
                        //todo 离开房间去除用户和房间的关联关系，但是房间需保留用户对象
                        AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
                        for (int i = 0; i < accountProfiles.length; i++) {
                            if(account.getId() == accountProfiles[i].getId()){
                                accountProfiles[i] = null;
                                break;
                            }
                        }
                        roomProfile.getPlayerNum().decrementAndGet();
                        //roomProfile = removeAccount(account.getRoomId(),account);
                        account.setRoomId(0);
                        //account.setRoomProfile(null);
                    }else{

                    }
                }
                return roomProfile;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return null;
    }

    public RoomProfile updateRoomStatus(Long roomId,int status){
        RoomProfile roomProfile = getByRoomId(roomId);
        if(roomProfile != null){
            if(RoomProfile.Status.CREATE.getCode() == status
                    && roomProfile.getRoomStatus() == RoomProfile.Status.CLOSE.getCode()){
                roomProfile.setRoomStatus(status);
            }else if(RoomProfile.Status.CLOSE.getCode() == status
                    && roomProfile.getRoomStatus() == RoomProfile.Status.CREATE.getCode()){
                roomProfile.setRoomStatus(status);
            }
            appRoomMap.get(roomProfile.getAppId()).put(roomProfile.getId(),roomProfile);
        }
        return roomProfile;
    }

    public RoomProfile destroyRoom(Long roomId){
        RoomProfile roomProfile = getByRoomId(roomId);
        if(roomProfile != null){
            AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
            for(AccountProfile accountProfile : accountProfiles){
                accountProfile.setRoomId(0);
                accountProfile.setStatus(AccountProfile.Status.ON_LINE.getCode());
                //accountProfile.setRoomProfile(null);
            }
            roomProfile.setAccountProfiles(null);
            removeRoom(roomId);
        }
        return roomProfile;
    }

    public void sendBroadcast(long roomId, long accountId, Message message){
        RoomProfile roomProfile = getByRoomId(roomId);
        if(roomProfile != null){
            //todo 塞选在游戏中的用户
            AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
            for(AccountProfile accountProfile : accountProfiles){
                if(accountId != accountProfile.getId()){
                    sendMessageToUser(accountProfile.getId(),message);
                }
            }
        }

    }


    public void sendMessageToUser(long accountId,Message message){
        IdSession session = SessionManager.INSTANCE.getSessionBy(accountId);
        if(session != null){
            LoggerUtils.error("发送消息：{},{}_{}",accountId,message.getModule(),message.getCmd());
            session.sendPacket(message);
        }
    }

    private RoomProfile removeAccount(Long roomId,AccountProfile account) {
        RoomProfile roomProfile = null;
        for(Map.Entry<String,Map<Long,RoomProfile>> entry : appRoomMap.entrySet()){
            if(entry != null && entry.getValue().containsKey(roomId)){
                roomProfile = entry.getValue().get(roomId);
                AccountProfile[] accountProfiles = roomProfile.getAccountProfiles();
                for(AccountProfile accountProfile : accountProfiles){
                    if(accountProfile.getId().equals(account.getId())){
                        accountProfile = null;
                        break;
                    }
                }
            }
        }
        if(roomProfile != null && roomProfile.isEmptyRoom()){
            appRoomMap.remove(roomId);
        }
        return roomProfile;
    }

    public RoomProfile getByRoomId(Long roomId) {
        for(Map.Entry<String,Map<Long,RoomProfile>> entry : appRoomMap.entrySet()){
            if(entry != null && entry.getValue().containsKey(roomId)){
                return entry.getValue().get(roomId);
            }
        }
        return null;
    }

    private void removeRoom(Long roomId) {
        for(Map.Entry<String,Map<Long,RoomProfile>> entry : appRoomMap.entrySet()){
            if(entry != null && entry.getValue().containsKey(roomId)){
                entry.getValue().remove(roomId);
            }
        }
    }



}
