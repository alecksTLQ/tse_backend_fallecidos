package com.example.springsocial.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;


@SuppressWarnings("serial")
@Entity
@Table(name="TREFDEPMUN")
@Where(clause = "CODMUN<>0")
public class TRefMun implements Serializable{
	
	
	@Column(name="CODDEP")
	private Long id;
	
	@Column(name="CODMUN")
	private Long codmun;
	
	@Id
	@Column(name="DESCRIPCION")
	private String DESCRIPCION;
	
	@Formula("(SELECT TREFDEPMUN.DESCRIPCION FROM TREFDEPMUN WHERE TREFDEPMUN.CODDEP=CODDEP "
			+ "AND TREFDEPMUN.CODMUN=CODMUN AND TREFDEPMUN.CODMUN<>0)")	
	private String DescripcionCalculada;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCodmun() {
		return codmun;
	}

	public void setCodmun(Long codmun) {
		this.codmun = codmun;
	}

	public String getDESCRIPCION() {
		return DESCRIPCION;
	}

	public void setDESCRIPCION(String dESCRIPCION) {
		DESCRIPCION = dESCRIPCION;
	}

	public String getDescripcionCalculada() {
		return DescripcionCalculada;
	}

	public void setDescripcionCalculada(String descripcionCalculada) {
		DescripcionCalculada = descripcionCalculada;
	}

	


	
}
