package com.example.springsocial.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

@Where(clause = "codmun=0")
@SuppressWarnings("serial")
@Entity
@Table(name="TREFDEPMUN")
public class DepartamentoModel implements Serializable{

	@Id
	@Column(name="coddep")
	private Long coddep;
	
	@Column(name="descripcion")
	private String nombre;

	
	
	
	public DepartamentoModel() {
	}

	public Long getCoddep() {
		return coddep;
	}

	public void setCoddep(Long coddep) {
		this.coddep = coddep;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
	/*
	 * 	CODDEP      NOT NULL NUMBER(5)    
		CODMUN      NOT NULL NUMBER(5)    
		DESCRIPCION          VARCHAR2(50) 
		ELECTORES            NUMBER(11) 
	 * */
	
}
