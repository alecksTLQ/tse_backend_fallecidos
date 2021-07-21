package com.example.springsocial.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="TDETALLEFOLIOH_N")
public class DetalleFolioHistoricoModelN implements Serializable{

	@Id
	@Column(name="ID")
	private Long ID;
	
	@Column(name="IDCABECERA")
	private Long IDCABECERA;
	
	
	@ManyToOne
	@JoinColumn(name="IDCABECERA", referencedColumnName="ID",insertable=false, updatable=false)
	private CabeceraFolioModelN IDCABECERAN;
	
	
	@Column(name="AÑO")
	private Integer AÑO;
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
	@Column(name="FECCRE")
	private Date FECCRE;
	@Column(name="USRCRE")
	private String USRCRE;
	@Column(name="USRVERIFI")
	private String USRVERIFI;
	@Column(name="FECMOD")
	private Date FECMOD;
	@Column(name="USRMOD")
	private String USRMOD;
	@Column(name="ESTATUS")
	private String ESTATUS;
	@Column(name="ESTATUSF") //8 || 4  SI FUE ACTUALIZADO ES DECIR ESTA EMPADRONADO || SI NO FUE ACTUALIZADO ES DECIR NO ESTA EMPADRONADO
	private Integer ESTATUSF;
	@Column(name="DEPTOINS")
	private Integer DEPTOINS;
	@Column(name="MUNICINS")
	private Integer MUNICINS;
	@Column(name="DEPTOEXT")
	private Integer DEPTOEXT;
	@Column(name="MUNICEXT")
	private Integer MUNICEXT;
	@Column(name="CUI")
	private Long CUI;
	@Column(name="ESTADODIFERENCIA")
	private Integer ESTADODIFERENCIA;
	@Column(name="COINCIDENCIAS")
	private String COINCIDENCIAS;
	@Column(name="REFERENCIAS")
	private String REFERENCIAS; // ALMACENA DATOS COMO = IDPRO, PUESTOMODIFICA QUE YA TENIA REGISTRADOS
	@Column(name="ESTATUSPREVIO")
	private String ESTATUSPREVIO;
	
	
	
	public DetalleFolioHistoricoModelN() {
	}
	
	
	
	public String getESTATUSPREVIO() {
		return ESTATUSPREVIO;
	}

	public void setESTATUSPREVIO(String eSTATUSPREVIO) {
		ESTATUSPREVIO = eSTATUSPREVIO;
	}

	public String getREFERENCIAS() {
		return REFERENCIAS;
	}

	public void setREFERENCIAS(String rEFERENCIAS) {
		REFERENCIAS = rEFERENCIAS;
	}

	public Integer getESTADODIFERENCIA() {
		return ESTADODIFERENCIA;
	}

	public void setESTADODIFERENCIA(Integer eSTADODIFERENCIA) {
		ESTADODIFERENCIA = eSTADODIFERENCIA;
	}

	public String getCOINCIDENCIAS() {
		return COINCIDENCIAS;
	}

	public void setCOINCIDENCIAS(String cOINCIDENCIAS) {
		COINCIDENCIAS = cOINCIDENCIAS;
	}

	public CabeceraFolioModelN getIDCABECERAN() {
		return IDCABECERAN;
	}

	public void setIDCABECERAN(CabeceraFolioModelN iDCABECERAN) {
		IDCABECERAN = iDCABECERAN;
	}

	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public Long getIDCABECERA() {
		return IDCABECERA;
	}
	public void setIDCABECERA(Long iDCABECERA) {
		IDCABECERA = iDCABECERA;
	}
	public Integer getAÑO() {
		return AÑO;
	}
	public void setAÑO(Integer aÑO) {
		AÑO = aÑO;
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
	public Date getFECCRE() {
		return FECCRE;
	}
	public void setFECCRE(Date fECCRE) {
		FECCRE = fECCRE;
	}
	public String getUSRCRE() {
		return USRCRE;
	}
	public void setUSRCRE(String uSRCRE) {
		USRCRE = uSRCRE;
	}
	public String getUSRVERIFI() {
		return USRVERIFI;
	}
	public void setUSRVERIFI(String uSRVERIFI) {
		USRVERIFI = uSRVERIFI;
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
	public String getESTATUS() {
		return ESTATUS;
	}
	public void setESTATUS(String eSTATUS) {
		ESTATUS = eSTATUS;
	}
	public Integer getESTATUSF() {
		return ESTATUSF;
	}
	public void setESTATUSF(Integer eSTATUSF) {
		ESTATUSF = eSTATUSF;
	}
	public Integer getDEPTOINS() {
		return DEPTOINS;
	}
	public void setDEPTOINS(Integer dEPTOINS) {
		DEPTOINS = dEPTOINS;
	}
	public Integer getMUNICINS() {
		return MUNICINS;
	}
	public void setMUNICINS(Integer mUNICINS) {
		MUNICINS = mUNICINS;
	}
	public Integer getDEPTOEXT() {
		return DEPTOEXT;
	}
	public void setDEPTOEXT(Integer dEPTOEXT) {
		DEPTOEXT = dEPTOEXT;
	}
	public Integer getMUNICEXT() {
		return MUNICEXT;
	}
	public void setMUNICEXT(Integer mUNICEXT) {
		MUNICEXT = mUNICEXT;
	}
	public Long getCUI() {
		return CUI;
	}
	public void setCUI(Long cUI) {
		CUI = cUI;
	}
	
	
}

