package com.kingston.jforgame.server.game.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.kingston.jforgame.common.utils.ClassScanner;
import com.kingston.jforgame.server.game.database.user.player.Player;
import com.kingston.jforgame.server.game.player.serializer.IPlayerPropSerializer;

public class PlayerSerializerUtil {

	private static List<IPlayerPropSerializer> propSerializers = new ArrayList<>();
	
	private final static String SCAN_PATH = "com.kingston.jforgame";
	
	static {
		Set<Class<?>> handler = ClassScanner.listAllSubclasses(SCAN_PATH, IPlayerPropSerializer.class);
		for (Class<?> clazz: handler) {
			try {
				propSerializers.add((IPlayerPropSerializer) clazz.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	public static void serialize(Player player) {
		for (IPlayerPropSerializer handler:propSerializers) {
			handler.serialize(player);
		}
	}
	
	public static void deserialize(Player player) {
		for (IPlayerPropSerializer handler:propSerializers) {
			handler.deserialize(player);
		}
	}
	
	
	
}
