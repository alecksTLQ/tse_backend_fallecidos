package com.example.springsocial.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="TCABECERAFOLIO")
@SequenceGenerator(name = "default_gen", sequenceName = "nrofolio", allocationSize = 1)
public class CabeceraFolioModell implements Serializable{

	//@EmbeddedId
	//private IdCabecera id;
	@GeneratedValue(generator="default_gen")
	@Id
	@Column(name="NROFOLIO")
	private Integer NROFOLIO;
	
	@Column(name="AÑOFOLIO")
	private Integer AÑOFOLIO;
	
	@Column(name="CODDEPTO")
	private Integer CODDEPTO;
	
	@Column(name="CODMUNIC")
	private Integer CODMUNIC;
	
	@Column(name="FECINICIO")
	private Date FECINICIO;
	
	@Column(name="FECFINALI")
	private Date FECFINALI;
	
	@Column(name="FECSISTE")
	private Date FECSISTE;
	
	@Column(name="USRACTUA")
	private String USRACTUA;
	
	@Column(name="FECACTUA")
	private Date FECACTUA;
	
	@Column(name="VERIFICA")
	private String VERIFICA;
	

	
		

	public CabeceraFolioModell() {
		this.getNROFOLIO();
	}

	public Integer getNROFOLIO() {
		return NROFOLIO;
	}

	public void setNROFOLIO(Integer nROFOLIO) {
		NROFOLIO = nROFOLIO;
	}

	public Integer getAÑOFOLIO() {
		return AÑOFOLIO;
	}

	public void setAÑOFOLIO(Integer aÑOFOLIO) {
		AÑOFOLIO = aÑOFOLIO;
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

	public Date getFECINICIO() {
		return FECINICIO;
	}

	public void setFECINICIO(Date fECINICIO) {
		FECINICIO = fECINICIO;
	}

	public Date getFECFINALI() {
		return FECFINALI;
	}

	public void setFECFINALI(Date fECFINALI) {
		FECFINALI = fECFINALI;
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

	public Date getFECACTUA() {
		return FECACTUA;
	}

	public void setFECACTUA(Date fECACTUA) {
		FECACTUA = fECACTUA;
	}

	public String getVERIFICA() {
		return VERIFICA;
	}

	public void setVERIFICA(String vERIFICA) {
		VERIFICA = vERIFICA;
	}
	
	
}