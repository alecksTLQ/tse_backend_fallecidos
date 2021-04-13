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
	private	byte[] firmCiudadano;
	
	@Column(name="huella")
	private	byte[] huellaCiudano;
	
	@Column(name="status")
	private	Long statusImagenes;

	public String getCui() {
		return cui;
	}

	public byte[] getFotoCiudadano() {
		return fotoCiudadano;
	}

	public byte[] getFirmCiudadano() {
		return firmCiudadano;
	}

	public byte[] getHuellaCiudano() {
		return huellaCiudano;
	}

	public Long getStatusImagenes() {
		return statusImagenes;
	}

	
	

	
	
}
	
	

