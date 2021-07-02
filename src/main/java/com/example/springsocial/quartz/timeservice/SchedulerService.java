package com.example.springsocial.quartz.timeservice;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.springsocial.tools.RestResponse;


@Service
public class SchedulerService {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
	private final Scheduler scheduler;
	@SuppressWarnings("rawtypes")
	private RestResponse response= new RestResponse();
	
	
	JobDetail jobDetail = null;
	Trigger trigger = null;
	JSONObject respuesta = new JSONObject();
	
	public SchedulerService(final Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends Job> RestResponse schedulerTarea(final Class<T> jobClass, final TimerInfo info) {
		
		try {
			
			jobDetail = ConfiguracionJobDetailAndTrigger.buildJobDetail(jobClass, info);
			trigger = ConfiguracionJobDetailAndTrigger.buildTrigger(jobClass, info);
			
			scheduler.scheduleJob(jobDetail, trigger);
			
			response.setData("Tarea: "+info.getNombreProceso());
			
		}catch(SchedulerException e) {
			LOG.error(e.getMessage(), e );
			response.setError(e.getMessage());
		}
		
		return response;
	}
	
	
	
	@PostConstruct
	public void init() {
		try {
			scheduler.start();
			
		}catch(Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	@PreDestroy
	public void preDestro() {
		try {
			scheduler.shutdown();
		}catch(Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
}
