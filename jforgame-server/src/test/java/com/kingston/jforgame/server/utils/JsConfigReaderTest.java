package com.kingston.jforgame.server.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.kingston.jforgame.common.utils.FileUtils;
import com.kingston.jforgame.server.FireWallConfig;

public class JsConfigReaderTest {

	@Test
	public void testReadConfig() throws Exception {
		InputStream inputStream = JsConfigReaderTest.class.getClassLoader().getResourceAsStream("configs/firewall.cfg.js");
		String content = FileUtils.readText(inputStream);
		
		FireWallConfig config = new FireWallConfig();
		Map<String, Object> params = new HashMap<>();
		params.put("configs", config);
		
		String response = JsScriptEngine.runCode(content, params);
		System.err.println(config);
	}
	
}
