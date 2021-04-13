package com.example.springsocial.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="historico_afiliacion", schema="WEBCERTIFICACION")
public class HistoricoAfiliacionModel implements Serializable{
		
	
	@Column(name="cui")
	private	String cui;
	
	@Column(name="nroboleta")
	private	Long numeroBoleta;
	
	@Column(name="organizacionpolitica")
	private	String organizacionPolitica;
	
	@Column(name="siglas")
	private	String siglas;
	
	@Column(name="fechaafiliacion")
	private	Date fechaDeAfiliacion;
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="hoja")
	private	Long numeroHoja;	
	
	@Column(name="linea")
	private	Long numeroLinea;
	
	@Column(name="fechaoperacion")
	private	Date fechaDeOperacion;
	
	@Column(name="fecharecepcion")
	private	Date fechaDeRecepcion;
	
	@Column(name="fecharenuncia")
	private	Date fechaDeRenuncia;

	@Column(name="DOCUMENTORENUNCIA")
	private	String documentoRenuncia;	
	
	@Column(name="fechadebaja")
	private	Date fechaDeBaja;
	


	public String getCui() {
		return cui;
	}

	public Long getNumeroBoleta() {
		return numeroBoleta;
	}

	public String getOrganizacionPolitica() {
		return organizacionPolitica;
	}

	public String getSiglas() {
		return siglas;
	}

	public Long getNumeroHoja() {
		return numeroHoja;
	}

	public Long getNumeroLinea() {
		return numeroLinea;
	}

	public String getDocumentoRenuncia() {
		return documentoRenuncia;
	}

	public Date getFechaDeAfiliacion() {
		return fechaDeAfiliacion;
	}

	public Date getFechaDeOperacion() {
		return fechaDeOperacion;
	}

	public Date getFechaDeBaja() {
		return fechaDeBaja;
	}

	public Date getFechaDeRenuncia() {
		return fechaDeRenuncia;
	}

	public Date getFechaDeRecepcion() {
		return fechaDeRecepcion;
	}

	
	
}
