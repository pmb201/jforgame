package com.kingston.jforgame.server.cross.core.match;

import java.io.IOException;

import com.google.gson.Gson;
import com.kingston.jforgame.common.utils.HttpUtil;
import com.kingston.jforgame.server.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MatchHttpUtil {

	private static final Logger log = LoggerFactory.getLogger(MatchHttpUtil.class);

	public static AbstractMatchMessage submit(AbstractMatchMessage request) throws IOException {
		String signature = request.getClass().getSimpleName();
		String data = new Gson().toJson(request);
		String param = HttpUtil.buildUrlParam("service", signature,
				"param", data);

		String url = ServerConfig.getInstance().getMatchUrl() + "?" + param;
		String resultJson = HttpUtil.get(url);
		log.info("向匹配服发送心跳");
		UrlResponse urlResponse = new Gson().fromJson(resultJson, UrlResponse.class);
		String respClazz = urlResponse.getAttachemt();
		Class<?> msgClazz = MatchMessageFactory.getInstance().getMessageBy(respClazz);
		AbstractMatchMessage msgResponse = (AbstractMatchMessage)new Gson().fromJson(urlResponse.getMessage(), msgClazz);
		return msgResponse;
	}

}
