package com.example.springsocial.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name="TIMAGENES", schema="IMGSRENAP")
@SequenceGenerator(name = "default_gen", sequenceName = "IDSERIEINVALIDO", allocationSize = 1)
public class ImagesRenapModel implements Serializable{
		
	@Id	
	@Column(name="cui")
	private	String cui;
	
	@Column(name="foto")
	private	byte[] fotoCiudadano;
	
	@Column(name="firma")
	private	byte[] firmaCiudadano;
	
	@Column(name="huella")
	private	byte[] huellaCiudadano;
	
	@Column(name="status")
	private	Long statusImagenes;

	public String getCui() {
		return cui;
	}

	public byte[] getFotoCiudadano() {
		return fotoCiudadano;
	}

	public byte[] getFirmaCiudadano() {
		return firmaCiudadano;
	}

	public byte[] getHuellaCiudadano() {
		return huellaCiudadano;
	}

	public Long getStatusImagenes() {
		return statusImagenes;
	}

	
	

	
	
}
	
	

