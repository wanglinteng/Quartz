package com.bigcan.quartz;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import linteng.wang.model.Remind;
import linteng.wang.service.RemindService;
import linteng.wang.util.Util;

public class Manager implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Remind> reminds = new RemindService().getReminds();// 数据库读取提醒
		for (int i = 0; i < reminds.size(); i++) {
			Quartz.addJob(reminds.get(i).getTime());// 添加任务时间戳10位
			try {
				Util.ManagerLog(" 添加待执行任务，任务时间：【" + dateformat.format(new Date(reminds.get(i).getTime() * 1000)) + "】");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
