package com.bigcan.quartz;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import linteng.wang.model.Remind;
import linteng.wang.service.RemindService;
import linteng.wang.util.Util;

public class Send implements Job {
	/**
	 * 提醒操作
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取添加任务时的参数（时间）
		JobDataMap data = context.getJobDetail().getJobDataMap();
		Long time = data.getLong("time");
		List<Remind> reminds = new RemindService().loadByTime(time);// 数据库读取time时刻提醒
		for (int i = 0; i < reminds.size(); i++) {
			// 执行post请求微信服务器
			int id = reminds.get(i).getId();
			String response = Util.loadJSON("http://ly.linteng.wang/xs/home/index/send?key=wanglinteng&id=" + id);
			try {
				Util.SendLog(" 执行时间为：" + dateformat.format(new Date(time * 1000)) + " 的定时任务，提醒ID为:【"+id+"】，返回值：" + response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Quartz.removeJob(time);// 执行完立即移除自己的触发器
	}
}
