package linteng.wang.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	// 写调度日志文件
	public static void ManagerLog(String log) throws IOException {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		File file = new File("ManagerLog.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		String content = "\r\n" + time.format(new Date()) + log + "\r\n";// 换行、设置时间
		FileWriter writer = new FileWriter(file, true);
		writer.write(content);
		writer.close();
	}

	// 写入执行日志
	public static void SendLog(String log) throws IOException {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		File file = new File("SendLog.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		String content = "\r\n" + time.format(new Date()) + log + "\r\n";// 换行、设置时间
		FileWriter writer = new FileWriter(file, true);
		writer.write(content);
		writer.close();
	}

	// 请求返回json数据
	public static String loadJSON(String url) {
		StringBuilder json = new StringBuilder();
		try {
			URL oracle = new URL(url);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return json.toString();
	}
}
