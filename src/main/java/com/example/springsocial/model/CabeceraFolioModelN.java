package com.example.springsocial.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name="TCABECERAFOLIO_N")
public class CabeceraFolioModelN implements Serializable{
	
	@Id
	@Column(name="ID")
	private Long ID;
	
	@Column(name="IDPAQUETE") // no. folio
	private	Long IDPAQUETE;
	
	@Column(name="AÑO")
	private	Integer AÑOFOLIO;
	
	@Column(name="ORIGEN")
	private Integer ORIGEN;
	
	
	
	@Column(name="CODDEPTO")
	private Integer CODDEPTO;
	
	
	
	
	@Column(name="CODMUNIC")
	private Integer CODMUNIC;
	
	

	@Column(name="USRCRE")
	private String USRACTUA;
	
	@Column(name="FECCRE")
	private Date FECCRE;
	
	@Column(name="FECMOD")
	private Date FECMOD;
	
	@Column(name="USRMOD")
	private String USRMOD;
	
	@Column(name="LINEAFOLIO")
	private Integer LINEAFOLIO;
	
	@Column(name="VERIFICA")
	private String VERIFICA;
	
	@Column(name="FECHAENTREGA")
	private Date FECHAENTREGA;
	
	
	
	public CabeceraFolioModelN() {
	}	

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public Long getIDPAQUETE() {
		return IDPAQUETE;
	}

	public void setIDPAQUETE(Long iDPAQUETE) {
		IDPAQUETE = iDPAQUETE;
	}

	public Date getFECHAENTREGA() {
		return FECHAENTREGA;
	}

	public void setFECHAENTREGA(Date fECHAENTREGA) {
		FECHAENTREGA = fECHAENTREGA;
	}

	public Integer getAÑOFOLIO() {
		return AÑOFOLIO;
	}

	public void setAÑOFOLIO(Integer aÑOFOLIO) {
		AÑOFOLIO = aÑOFOLIO;
	}

	public Date getFECCRE() {
		return FECCRE;
	}

	public void setFECCRE(Date fECCRE) {
		FECCRE = fECCRE;
	}

	public Integer getLINEAFOLIO() {
		return LINEAFOLIO;
	}

	public void setLINEAFOLIO(Integer lINEAFOLIO) {
		LINEAFOLIO = lINEAFOLIO;
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
