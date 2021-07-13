package com.example.springsocial.quartz;

import javax.persistence.EntityManagerFactory;

import org.apache.catalina.connector.Request;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.quartz.jobs.CaptacionAutomaticaJob;
import com.example.springsocial.quartz.timeservice.SchedulerService;
import com.example.springsocial.quartz.timeservice.TimerInfo;
import com.example.springsocial.repository.CabeceraFolioRepositoryN;
import com.example.springsocial.repository.DetalleFolioRepositoryN;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.RestResponse;


@SuppressWarnings({"rawtypes","unchecked","unused"})
@Service
public class PlayService {
	
	private EntityManagerFactory entityManagerFactory;
	private CabeceraFolioRepositoryN rpCabeceraFolio;
	private DetalleFolioRepositoryN rpDetalleFolio;
	private UserPrincipal userPrincipal =null;
	private Object createElement=null;
	private String token = null;
	private RestResponse response= new RestResponse();
	
	private final SchedulerService scheduler;
	private JSONObject respuesta = new JSONObject();
	private ObjectSetGet data= new ObjectSetGet();
	private TimerInfo info = null;
	
	
	public PlayService(final SchedulerService scheduler) {this.scheduler = scheduler;}
	public void setToken(String token) {	this.token = token;}
	public void setUserPrincipal(UserPrincipal userPrincipal) {this.userPrincipal=userPrincipal;}
	public void setData(Object createElement) {data.setObject(createElement);}
	public void setRepository(CabeceraFolioRepositoryN rpCabeceraFolio, DetalleFolioRepositoryN rpDetalleFolio) {
		this.rpCabeceraFolio = rpCabeceraFolio;
		this.rpDetalleFolio = rpDetalleFolio;
	}
	
	public RestResponse getResponse() {return this.response; }
	
	public void runTarea() throws Exception {
		this.info = new TimerInfo();
		
		try {
			info.setIdProceso("id: CaptacionFallecidosAutomatico");
			info.setNombreProceso("Tarea Captacion Fallecidos");
			info.setIntervaloEjecucion("");
			info.setDescripcion("Obtener Datos de API Defunciones, verificacion en TPADRON y posterior insercion");
			info.setToken(this.token);
			info.setHora(4);
			info.setUserPrincipal(userPrincipal.getUsername());
			info.setFecha(data.getValue("fecha").toString());
			info.setRpCabeceraFolio(rpCabeceraFolio);
			info.setRpDetalleFolio(rpDetalleFolio);
			response.setData(scheduler.schedulerTarea(CaptacionAutomaticaJob.class, info));
		}catch(Exception e) {
			response.setError(e.getMessage());
		}
	}
}
