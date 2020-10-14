package com.kingston.jforgame.server.game.play.record;

import com.kingston.jforgame.server.cache.BaseCacheService;
import com.kingston.jforgame.server.db.DbService;
import com.kingston.jforgame.server.db.DbUtils;
import com.kingston.jforgame.server.game.play.record.entity.PlayRecord;

/**
 * @Author puMengBin
 * @Date 2020-10-13 09:46
 * @Description
 */
public class PlayRecordManager extends BaseCacheService<Long, PlayRecord> {
    @Override
    public PlayRecord load(Long aLong) throws Exception {
        String sql = "SELECT * FROM PlayRecord where Id = ? ";
//		sql = MessageFormat.format(sql, String.valueOf(playerId));
        PlayRecord record = DbUtils.queryOneById(DbUtils.DB_USER, sql, PlayRecord.class, String.valueOf(aLong));
        if (record != null) {
            record.doAfterInit();
        }
        return record;
    }

    public PlayRecord save(PlayRecord record)throws Exception{
        DbService.getInstance().insertOrUpdate(record);
        return record;
    }


}
