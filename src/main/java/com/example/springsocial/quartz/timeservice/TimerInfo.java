package com.example.springsocial.quartz.timeservice;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.springsocial.repository.CabeceraFolioRepositoryN;
import com.example.springsocial.repository.DetalleFolioRepositoryN;

public class TimerInfo {
	
	private String idProceso;
	private String nombreProceso;
	private String IntervaloEjecucion;
	private String descripcion;
	
	private String fecha;
	private String token;
	private String userPrincipal;
	
	private CabeceraFolioRepositoryN rpCabeceraFolio;
	
	private DetalleFolioRepositoryN rpDetalleFolio;
	
	
	public TimerInfo() {
	}
	
	
	
	public CabeceraFolioRepositoryN getRpCabeceraFolio() {
		return rpCabeceraFolio;
	}

	public void setRpCabeceraFolio(CabeceraFolioRepositoryN rpCabeceraFolio) {
		this.rpCabeceraFolio = rpCabeceraFolio;
	}

	public DetalleFolioRepositoryN getRpDetalleFolio() {
		return rpDetalleFolio;
	}

	public void setRpDetalleFolio(DetalleFolioRepositoryN rpDetalleFolio) {
		this.rpDetalleFolio = rpDetalleFolio;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserPrincipal() {
		return userPrincipal;
	}

	public void setUserPrincipal(String userPrincipal) {
		this.userPrincipal = userPrincipal;
	}

	public String getIdProceso() {
		return idProceso;
	}
	public void setIdProceso(String idProceso) {
		this.idProceso = idProceso;
	}
	public String getNombreProceso() {
		return nombreProceso;
	}
	public void setNombreProceso(String nombreProceso) {
		this.nombreProceso = nombreProceso;
	}
	public String getIntervaloEjecucion() {
		return IntervaloEjecucion;
	}
	public void setIntervaloEjecucion(String intervaloEjecucion) {
		IntervaloEjecucion = intervaloEjecucion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
