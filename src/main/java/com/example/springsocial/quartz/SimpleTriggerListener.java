package com.example.springsocial.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.*;

import org.quartz.TriggerListener;

import com.example.springsocial.quartz.timeservice.SchedulerService;
import com.example.springsocial.quartz.timeservice.TimerInfo;

public class SimpleTriggerListener  implements TriggerListener{

	@SuppressWarnings("unused")
	private final SchedulerService schedulerService;
	
	public SimpleTriggerListener(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return SimpleTriggerListener.class.getSimpleName();
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		// TODO Auto-generated method stub
		final String timerId = trigger.getKey().getName();

        final JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        final TimerInfo info = (TimerInfo) jobDataMap.get(timerId);

        if (info.getHora()<22) {
            int contadorhora = info.getHora();
            
            info.setHora(contadorhora+2);
            
            SchedulerService.updateTimer(timerId, info);
        }else {
        	System.out.println("proceso detenido");
        	SchedulerService.deleteJob(timerId);
        }
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		// TODO Auto-generated method stub
		
	}

}
