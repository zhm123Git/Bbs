package com.github.pig.bbs.util;

import com.baidu.aip.speech.AipSpeech;
import org.json.JSONObject;

public class VoiceUtil {
	//设置APPID/AK/SK
	public static final String APP_ID = "15464358";
	public static final String API_KEY = "p9voE2essDwcpsyXLDDnHUkM";
	public static final String SECRET_KEY = "w9GaWKISQNgYzrmrh7iXUZ9knQKVxHrr";

	public static Object  voiiceZtest( String Path ){
		// 初始化一个AipSpeech
		AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);

		// 可选：设置代理服务器地址, http和socket二选一，或者均不设置
/*
		client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
		client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
*/
		// 可选：设置log4j日志输出格式，若不设置，则使用默认配置
		// 也可以直接通过jvm启动参数设置此环境变量
		//	System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
		// 调用接口
		String targetPath ="";
		try {
			//System.out.println("文件读取路径：：："+Path);
			targetPath = changeAllToPcm.changeAllToPcmMethod(Path);
			System.out.println("文件生成::"+targetPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject res =null;
		if(!"".equals(targetPath)) {
			 res = client.asr(targetPath, "pcm", 16000, null);
			System.out.println("返回成功:::"+res.toString());
//			System.out.println(res.toString(2));
		}
		return res.toString();
	}




}
