package com.github.pig.bbs.util;

import java.io.IOException;
import java.util.UUID;

public class changeAllToPcm {//"/monchickey/ffmpeg/bin"; //
	static String  ffmpegPath=  "/monchickey/ffmpeg/bin"; //"E:/isCH/voice/ffmpeg-20190120-62f8d27-win64-static/bin";
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot >-1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/*public static void main(String[] args) throws Exception{
		changeAllToPcmMethod("","");
	}*/

	public static String  changeAllToPcmMethod(String sourcePath) throws Exception {

		System.out.println("sourcePath:::"+sourcePath);
		String extensionName = getExtensionName(sourcePath);
		System.out.println("extensionName::"+extensionName);

		if("pcm".equals(extensionName) ){
			return sourcePath;
		}

		String uid = UUID.randomUUID().toString().replaceAll("-", "");
		String  targetPath = "/home/test/"+uid+ ".pcm";

		Runtime run = null;
		Process process = null;
		try {
			run = Runtime.getRuntime();
			long start = System.currentTimeMillis();
			//System.out.println("ffmpeg的exe执行文件地址:"+new File(webroot).getAbsolutePath());
			//执行ffmpeg.exe,前面是ffmpeg.exe的地址，中间是需要转换的文件地址，后面是转换后的文件地址。-i是转换方式，意思是可编码解码，mp3编码方式采用的是libmp3lame
			//wav转pcm
			//Process p=run.exec(new File(webroot).getAbsolutePath()+"/ffmpeg -y -i "+sourcePath+" -acodec pcm_s16le -f s16le -ac 1 -ar 16000 "+targetPath);
			//mp3转pcm
			//	String s = new File(webroot).getAbsolutePath() + "/ffmpeg -y -i " + sourcePath + " -ar 44100 -ac 2 -acodec mp3 " + targetPath;
			//E:/isCH/test1.mp3E:/isCH/test1.pcm
			String cmd =ffmpegPath+"/ffmpeg -y  -i "+sourcePath+"  -acodec pcm_s16le -f s16le -ac 1 -ar 16000 "+targetPath;
			//执行命令
			System.out.println("ffmpeg 执行命令"+cmd);

			process = run.exec(new String[]{"sh","-c",cmd});
			process.waitFor();
	/*win
			Process exec = run.exec(cmd);
			exec.waitFor();*/
			long end = System.currentTimeMillis();
			System.out.println(sourcePath + " 执行成功时间:" + (end - start) + "ms");


		} catch (Exception e) {
			System.out.println("音频文件转换异常 ， 请检查文件或者ffmpeg");
			e.printStackTrace();
		} finally {
			//释放进程
			try {
				process.getOutputStream().close();
				process.getInputStream().close();
				process.getErrorStream().close();
			} catch (Exception e) {
			//	e.printStackTrace();
				System.err.println("进程关闭异常,下一步");
			}

			//run调用lame解码器最后释放内存
			run.freeMemory();
		}
		System.out.println("生成的文件路径：："+targetPath);
		return targetPath;
	}

}
