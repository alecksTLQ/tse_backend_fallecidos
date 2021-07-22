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
@Where(clause = "CODMUN=0")
public class TRefDep implements Serializable{
	
	@Id
	@Column(name="CODDEP")
	private Integer id;
	
	@Column(name="CODMUN")
	private Integer CODMUN;
	
	@Formula("(SELECT TREFDEPMUN.DESCRIPCION FROM TREFDEPMUN WHERE TREFDEPMUN.CODDEP=CODDEP "
			+ "AND TREFDEPMUN.CODMUN=CODMUN AND TREFDEPMUN.CODMUN=0)")	
	@Column(name="DESCRIPCION")
	private String DESCRIPCION;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getCODMUN() {
		return CODMUN;
	}


	public void setCODMUN(Integer cODMUN) {
		CODMUN = cODMUN;
	}


	public String getDESCRIPCION() {
		return DESCRIPCION;
	}


	public void setDESCRIPCION(String dESCRIPCION) {
		DESCRIPCION = dESCRIPCION;
	}
	

	
	

}
