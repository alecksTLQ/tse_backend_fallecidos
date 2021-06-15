package com.example.springsocial.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Embeddable
@SequenceGenerator(name = "default_gen", sequenceName = "folio", allocationSize = 1)
public class IdCabecera implements Serializable{

	
	private static final long serialVersionUID = 1L;
	@GeneratedValue(generator="default_gen")
	@Column(name="NROFOLIO")
	private	Long NROFOLIO;
	
	@Column(name="AÑOFOLIO")
	private	Integer AÑOFOLIO;
	
	
	
	public IdCabecera() {
	}

	public IdCabecera(Long nROFOLIO, Integer aÑOFOLIO) {
		NROFOLIO = nROFOLIO;
		AÑOFOLIO = aÑOFOLIO;
	}

	public Long getNROFOLIO() {
		return NROFOLIO;
	}

	public void setNROFOLIO(Long nROFOLIO) {
		NROFOLIO = nROFOLIO;
	}

	public Integer getAÑOFOLIO() {
		return AÑOFOLIO;
	}

	public void setAÑOFOLIO(Integer aÑOFOLIO) {
		AÑOFOLIO = aÑOFOLIO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((AÑOFOLIO == null) ? 0 : AÑOFOLIO.hashCode());
		result = prime * result + ((NROFOLIO == null) ? 0 : NROFOLIO.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdCabecera other = (IdCabecera) obj;
		if (AÑOFOLIO == null) {
			if (other.AÑOFOLIO != null)
				return false;
		} else if (!AÑOFOLIO.equals(other.AÑOFOLIO))
			return false;
		if (NROFOLIO == null) {
			if (other.NROFOLIO != null)
				return false;
		} else if (!NROFOLIO.equals(other.NROFOLIO))
			return false;
		return true;
	}

	
	
}






		
	

