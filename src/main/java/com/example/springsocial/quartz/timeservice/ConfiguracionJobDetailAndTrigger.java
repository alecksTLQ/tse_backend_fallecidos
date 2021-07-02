package com.example.springsocial.quartz.timeservice;

import java.text.ParseException;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ConfiguracionJobDetailAndTrigger {
	
	public static JobDetail buildJobDetail(final Class jobClass, final TimerInfo info) {
		
		final JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(jobClass.getSimpleName(), info);
		
		return JobBuilder.newJob(jobClass).withIdentity(jobClass.getSimpleName()).setJobData(jobDataMap).build();
	}
	
	public static Trigger buildTrigger(final Class jobClass, final TimerInfo info) {
		Trigger trigger =  null;
		//* * 6,7,8,10,11,12,13,14,15 ? * * *
		//0 * * ? * * *
		//
		try {
			
			trigger = TriggerBuilder
					.newTrigger()
					.withIdentity(jobClass.getSimpleName())
					.withSchedule( CronScheduleBuilder.cronSchedule(new CronExpression("0 */2 * ? * *")) )
					.build();
			
		}catch(ParseException e) {
			e.printStackTrace();
		}
		
		return trigger;
	}

}
