package com.bigcan.quartz;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class Quartz {
	private static SchedulerFactory SchedulerFactory = new StdSchedulerFactory();// 调度工厂
	private static String JOB_GROUP_NAME = "SEND_JOBGROUP_NAME";// 提醒任务组
	private static String TRIGGER_GROUP_NAME = "SEND_TRIGGERGROUP_NAME";// 提醒触发器组

	/**
	 * @see 主函数、开启服务
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		JobDetail jobDetail = JobBuilder.newJob(Manager.class).withIdentity("Jmanager", "MANAGER").build();
		// 动态调度任务
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("Tmanager", "MANAGER")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 02 10/1 * * ? ")).build();
		Scheduler scheduler = SchedulerFactory.getScheduler();
		// 添加job，以及其关联的trigger
		scheduler.scheduleJob(jobDetail, trigger);
		// 启动job
		scheduler.start();
		// 设置UI
		new SystemUi();
	}

	/**
	 * @see 添加提醒触发器、任务
	 * @param time
	 */
	public static void addJob(long time) {
		try {
			Scheduler scheduler = SchedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(Send.class).withIdentity("Job_" + time, JOB_GROUP_NAME).build();// 任务名称Job_时间
			SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
					.withIdentity("Trigger_" + time, TRIGGER_GROUP_NAME).startAt(new Date(time * 1000)).build();// 触发器名称Trigger_时间
																												// 转化为13位时间戳
			jobDetail.getJobDataMap().put("time", time);// 向任务传递参数（时间）
			scheduler.scheduleJob(jobDetail, trigger);// 任务、触发器绑定
			// 启动此调度
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see 移除提醒触发器、任务
	 * @param time
	 */
	public static void removeJob(Long time) {
		try {
			Scheduler scheduler = SchedulerFactory.getScheduler();

			TriggerKey triggerKey = TriggerKey.triggerKey("Trigger_" + time, TRIGGER_GROUP_NAME);
			scheduler.pauseTrigger(triggerKey);// 暂停触发器
			scheduler.unscheduleJob(triggerKey);// 移除触发器

			JobKey jobKey = JobKey.jobKey("Job_" + time, JOB_GROUP_NAME);
			scheduler.pauseJob(jobKey);// 停止任务
			scheduler.deleteJob(jobKey);// 移除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
