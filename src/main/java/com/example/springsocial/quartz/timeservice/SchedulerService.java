package com.example.springsocial.quartz.timeservice;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.springsocial.quartz.SimpleTriggerListener;
import com.example.springsocial.tools.RestResponse;


@Service
public class SchedulerService {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
	private static Scheduler scheduler = null;
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
	
	public static void updateTimer(final String timerId, final TimerInfo info) {
        try {
            final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(timerId));
            if (jobDetail == null) {
                LOG.error("Failed to find timer with ID '{}'", timerId);
                return;
            }

            jobDetail.getJobDataMap().put(timerId, info);

            scheduler.addJob(jobDetail, true, true);
        } catch (final SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }
	
	public static void deleteJob(final String timerId) {
		try{
			scheduler.deleteJob(new JobKey(timerId));
		}catch(Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	
	
	@PostConstruct
	public void init() {
		try {
			scheduler.start();
			scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener(this));
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
