package com.kingston.jforgame.orm.utils;

import com.kingston.jforgame.orm.OrmBridge;
import com.kingston.jforgame.orm.OrmProcessor;
import com.kingston.jforgame.orm.SqlFactory;
import com.kingston.jforgame.orm.cache.AbstractCacheable;

public class SqlUtils {

	public static String getInsertSql(AbstractCacheable entity) {
		OrmBridge bridge = OrmProcessor.INSTANCE.getOrmBridge(entity.getClass());
		return SqlFactory.createInsertSql(entity, bridge);
	}

	public static String getUpdateSql(AbstractCacheable entity) {
		OrmBridge bridge = OrmProcessor.INSTANCE.getOrmBridge(entity.getClass());
		return SqlFactory.createUpdateSql(entity, bridge);
	}

	public static String getDeleteSql(AbstractCacheable entity) {
		OrmBridge bridge = OrmProcessor.INSTANCE.getOrmBridge(entity.getClass());
		return SqlFactory.createDeleteSql(entity, bridge);
	}
	
	public static String getSaveSql(AbstractCacheable entity) {
		if (entity.isInsert()) {
			return getInsertSql(entity);
		} else if (entity.isUpdate()) {
			return getUpdateSql(entity);
		} else if (entity.isDelete()) {
			return getDeleteSql(entity);
		}
		return "";
	}

}
