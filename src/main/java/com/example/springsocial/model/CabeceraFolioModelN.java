package com.example.springsocial.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name="TCABECERAFOLION")
public class CabeceraFolioModelN implements Serializable{
	
	@EmbeddedId
    private Idpaquete id;
	
	@Column(name="ORIGEN")
	private Integer ORIGEN;
	
	@Column(name="CODDEPTO")
	private Integer CODDEPTO;
	
	@Column(name="CODMUNIC")
	private Integer CODMUNIC;
	
	@Column(name="FECHASISTE")
	private Date FECSISTE;

	@Column(name="USRACTUA")
	private String USRACTUA;
	
	@Column(name="VERIFICA")
	private String VERIFICA;
	
	@Column(name="LINEA")
	private Integer LINEA;
	
	@Column(name="FECMOD")
	private Date FECMOD;
	
	@Column(name="USRMOD")
	private String USRMOD;
	
	
	/*
	 * @ManyToOne
		@JoinColumn(name="depsolicitud", referencedColumnName="coddep", insertable=false, updatable=false)
	    private DepartamentoVista departamentoSolicitud;
	 * */

	public CabeceraFolioModelN() {
	}
	
	

	public Date getFECMOD() {
		return FECMOD;
	}

	public void setFECMOD(Date fECMOD) {
		FECMOD = fECMOD;
	}

	public String getUSRMOD() {
		return USRMOD;
	}

	public void setUSRMOD(String uSRMOD) {
		USRMOD = uSRMOD;
	}

	public Integer getLINEA() {
		return LINEA;
	}

	public void setLINEA(Integer lINEA) {
		LINEA = lINEA;
	}

	public Idpaquete getId() {
		return id;
	}

	public void setId(Idpaquete id) {
		this.id = id;
	}

	public Integer getORIGEN() {
		return ORIGEN;
	}

	public void setORIGEN(Integer oRIGEN) {
		ORIGEN = oRIGEN;
	}

	public Integer getCODDEPTO() {
		return CODDEPTO;
	}

	public void setCODDEPTO(Integer cODDEPTO) {
		CODDEPTO = cODDEPTO;
	}

	public Integer getCODMUNIC() {
		return CODMUNIC;
	}

	public void setCODMUNIC(Integer cODMUNIC) {
		CODMUNIC = cODMUNIC;
	}

	public Date getFECSISTE() {
		return FECSISTE;
	}

	public void setFECSISTE(Date fECSISTE) {
		FECSISTE = fECSISTE;
	}

	public String getUSRACTUA() {
		return USRACTUA;
	}

	public void setUSRACTUA(String uSRACTUA) {
		USRACTUA = uSRACTUA;
	}

	public String getVERIFICA() {
		return VERIFICA;
	}

	public void setVERIFICA(String vERIFICA) {
		VERIFICA = vERIFICA;
	}
}
