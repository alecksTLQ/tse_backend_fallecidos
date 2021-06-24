package com.example.springsocial.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name="TDETALLEFOLIO")
public class DetalleFolioModel implements Serializable{
	
	@Id
	@Column(name="IDPAQUETE")
	private Integer IDPAQUETE;
	
	@Column(name="AÑOFOLIO")
	private Integer AÑOFOLIO; 
	
	@Column(name="NROLINEA")
	private Integer NROLINEA;
	
	@Column(name="APE1FALLE")
	private String APE1FALLE; 
	@Column(name="APE2FALLE")
	private String APE2FALLE;
	@Column(name="APE3FALLE")
	private String APE3FALLE; 
	@Column(name="NOM1FALLE")
	private String NOM1FALLE;
	@Column(name="NOM2FALLE")
	private String NOM2FALLE; 
	@Column(name="APE1PADRE")
	private String APE1PADRE; 
	@Column(name="APE2PADRE")
	private String APE2PADRE;  
	@Column(name="NOMPADRE")
	private String NOMPADRE; 
	@Column(name="APE1MADRE")
	private String APE1MADRE;
	@Column(name="APE2MADRE")
	private String APE2MADRE;  
	@Column(name="NOMMADRE")
	private String NOMMADRE;   
	@Column(name="FECHADEFU")
	private Date FECHADEFU; 
	@Column(name="NROORDEN")
	private String NROORDEN; 
	@Column(name="NROREGIST")
	private Integer NROREGIST;
	@Column(name="NROBOLETA")
	private Integer NROBOLETA;  
	@Column(name="FECHANACI")
	private Date FECHANACI;   
	@Column(name="FECOPERA")
	private Date FECOPERA;   
	@Column(name="USRDIGITA")
	private String USRDIGITA; 
	@Column(name="USRVERIFI")
 	private String USRVERIFI;  
	@Column(name="FECHACORD")
	private Date FECHACORD;
	@Column(name="ESTACOND")
	private String ESTACOND;
	
	@Column(name="ESTATUS")
	private String ESTATUS;  
	@Column(name="CUI")
	private Long CUI;
	public Integer getIDPAQUETE() {
		return IDPAQUETE;
	}
	public void setIDPAQUETE(Integer iDPAQUETE) {
		IDPAQUETE = iDPAQUETE;
	}
	public Integer getAÑOFOLIO() {
		return AÑOFOLIO;
	}
	public void setAÑOFOLIO(Integer aÑOFOLIO) {
		AÑOFOLIO = aÑOFOLIO;
	}
	public Integer getNROLINEA() {
		return NROLINEA;
	}
	public void setNROLINEA(Integer nROLINEA) {
		NROLINEA = nROLINEA;
	}
	public String getAPE1FALLE() {
		return APE1FALLE;
	}
	public void setAPE1FALLE(String aPE1FALLE) {
		APE1FALLE = aPE1FALLE;
	}
	public String getAPE2FALLE() {
		return APE2FALLE;
	}
	public void setAPE2FALLE(String aPE2FALLE) {
		APE2FALLE = aPE2FALLE;
	}
	public String getAPE3FALLE() {
		return APE3FALLE;
	}
	public void setAPE3FALLE(String aPE3FALLE) {
		APE3FALLE = aPE3FALLE;
	}
	public String getNOM1FALLE() {
		return NOM1FALLE;
	}
	public void setNOM1FALLE(String nOM1FALLE) {
		NOM1FALLE = nOM1FALLE;
	}
	public String getNOM2FALLE() {
		return NOM2FALLE;
	}
	public void setNOM2FALLE(String nOM2FALLE) {
		NOM2FALLE = nOM2FALLE;
	}
	public String getAPE1PADRE() {
		return APE1PADRE;
	}
	public void setAPE1PADRE(String aPE1PADRE) {
		APE1PADRE = aPE1PADRE;
	}
	public String getAPE2PADRE() {
		return APE2PADRE;
	}
	public void setAPE2PADRE(String aPE2PADRE) {
		APE2PADRE = aPE2PADRE;
	}
	public String getNOMPADRE() {
		return NOMPADRE;
	}
	public void setNOMPADRE(String nOMPADRE) {
		NOMPADRE = nOMPADRE;
	}
	public String getAPE1MADRE() {
		return APE1MADRE;
	}
	public void setAPE1MADRE(String aPE1MADRE) {
		APE1MADRE = aPE1MADRE;
	}
	public String getAPE2MADRE() {
		return APE2MADRE;
	}
	public void setAPE2MADRE(String aPE2MADRE) {
		APE2MADRE = aPE2MADRE;
	}
	public String getNOMMADRE() {
		return NOMMADRE;
	}
	public void setNOMMADRE(String nOMMADRE) {
		NOMMADRE = nOMMADRE;
	}
	public Date getFECHADEFU() {
		return FECHADEFU;
	}
	public void setFECHADEFU(Date fECHADEFU) {
		FECHADEFU = fECHADEFU;
	}
	public String getNROORDEN() {
		return NROORDEN;
	}
	public void setNROORDEN(String nROORDEN) {
		NROORDEN = nROORDEN;
	}
	public Integer getNROREGIST() {
		return NROREGIST;
	}
	public void setNROREGIST(Integer nROREGIST) {
		NROREGIST = nROREGIST;
	}
	public Integer getNROBOLETA() {
		return NROBOLETA;
	}
	public void setNROBOLETA(Integer nROBOLETA) {
		NROBOLETA = nROBOLETA;
	}
	public Date getFECHANACI() {
		return FECHANACI;
	}
	public void setFECHANACI(Date fECHANACI) {
		FECHANACI = fECHANACI;
	}
	public Date getFECOPERA() {
		return FECOPERA;
	}
	public void setFECOPERA(Date fECOPERA) {
		FECOPERA = fECOPERA;
	}
	public String getUSRDIGITA() {
		return USRDIGITA;
	}
	public void setUSRDIGITA(String uSRDIGITA) {
		USRDIGITA = uSRDIGITA;
	}
	public String getUSRVERIFI() {
		return USRVERIFI;
	}
	public void setUSRVERIFI(String uSRVERIFI) {
		USRVERIFI = uSRVERIFI;
	}
	public Date getFECHACORD() {
		return FECHACORD;
	}
	public void setFECHACORD(Date fECHACORD) {
		FECHACORD = fECHACORD;
	}
	public String getESTACOND() {
		return ESTACOND;
	}
	public void setESTACOND(String eSTACOND) {
		ESTACOND = eSTACOND;
	}
	public String getESTATUS() {
		return ESTATUS;
	}
	public void setESTATUS(String eSTATUS) {
		ESTATUS = eSTATUS;
	}
	public Long getCUI() {
		return CUI;
	}
	public void setCUI(Long cUI) {
		CUI = cUI;
	}
	public DetalleFolioModel() {
	}
	
	
	
	
	
}
