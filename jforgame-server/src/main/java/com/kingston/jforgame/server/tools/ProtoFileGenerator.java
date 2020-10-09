package com.kingston.jforgame.server.tools;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import com.kingston.jforgame.common.utils.ClassScanner;
import com.kingston.jforgame.socket.message.Message;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Set;

/**
 * 导出.proto文件(供客户端用)
 * @author kingston
 */
public class ProtoFileGenerator {

	public static void main(String[] args) {
		String rootPath = "com.kingston.jforgame.server.game.collision.message";
		Set<Class<?>> messages = ClassScanner.listAllSubclasses(rootPath, Message.class);
		writeProtoFile(messages);
	}

	private static void writeProtoFile(Set<Class<?>> sourceClazzs){
		for(Class clazz:sourceClazzs){
			String filePath = MessageFormat.format("{0}.proto", new Object[]{clazz.getSimpleName()});
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
				String source = ProtobufIDLGenerator.getIDL(clazz);
				String result = "";
				for (String s : source.split("\n")) {
					if (s.startsWith("package ") || s.startsWith("option ")) {
						continue;
					}
					result += s + "\n";
				}
				writer.write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		System.out.println("completed");
	}

}
