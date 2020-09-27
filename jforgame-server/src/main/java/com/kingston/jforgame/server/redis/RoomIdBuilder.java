package com.kingston.jforgame.server.redis;



import java.time.LocalDate;


/**
 * @Author puMengBin
 * @Date 2020-09-24 16:48
 * @Description
 */
public class RoomIdBuilder {


    public static final long start = 10000;

    private static final String ROOM_ID_KEY_PRE = "xiyou:game_server:room_id:";


    private static final RedisCluster cluster = RedisCluster.INSTANCE;

    public static long buildRoomId(){
        LocalDate localDate = LocalDate.now();
        String key = ROOM_ID_KEY_PRE + localDate.toString();
        int day = localDate.getDayOfMonth();

        return day * start + cluster.getId(key);
    }



}
