package com.example.springsocial.quartz.jobs;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.apache.catalina.connector.Request;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONException;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.process.CaptacionFallecido_RenapDefuncionesProcess;
import com.example.springsocial.quartz.PlayService;
import com.example.springsocial.quartz.timeservice.TimerInfo;
import com.example.springsocial.repository.CabeceraFolioRepositoryN;
import com.example.springsocial.repository.DetalleFolioHistoricoRepository;
import com.example.springsocial.repository.DetalleFolioRepositoryN;
import com.example.springsocial.security.UserPrincipal;


@SuppressWarnings("unused")
public class CaptacionAutomaticaJob implements Job{
	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private PlayService serviceQuartz;
	
	private CaptacionFallecido_RenapDefuncionesProcess captacion = new CaptacionFallecido_RenapDefuncionesProcess();
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		TimerInfo info = (TimerInfo) jobDataMap.get(CaptacionAutomaticaJob.class.getSimpleName());
		System.out.println("Hora de consulta: "+ info.getHora());
		try {
			tarea(info.getToken(), info.getUserPrincipal(), info.getFecha(), info.getRpCabeceraFolio(), info.getRpDetalleFolio(), info.getRpDetalleHistorico(),info.getHora());
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void tarea(String token, String user, String fecha, CabeceraFolioRepositoryN rpCabeceraFolio, DetalleFolioRepositoryN rpDetalleFolio, DetalleFolioHistoricoRepository rpDetalleHistorico, Integer Hora) throws CustomException {
		try {
			System.out.println("Tarea automatica: captacion ");
			captacion.setToken(token);
			captacion.setFecha(fecha);
			captacion.setHoraInicia(Hora);
			captacion.setUser(user);
			captacion.setEntityManagerFactory(this.entityManagerFactory);
			captacion.setRespository(rpCabeceraFolio, rpDetalleFolio, rpDetalleHistorico);
			captacion.ObtenerRegistrosDefuncionesRenap();
			
		}catch(JSONException e) {
			e.printStackTrace();
		}
	}

}
