package com.example.springsocial.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

@Where(clause = "departamen1_.codmun=0")
@SuppressWarnings("serial")
@Entity
@Table(name="TREFDEPMUN")
public class MunicipioModel implements Serializable{
	
	
	@Column(name="codmun")
	private Long codmun;
	
	@Column(name="coddep")
	private Long coddep;
	
	@ManyToOne
	@JoinColumn(name="coddep", insertable=false, updatable=false)
    private DepartamentoModel departamento;
	
	@Id
	@Column(name="descripcion")
	private String nombre;
	
	
	
	public MunicipioModel() {
	}

	public Long getCodmun() {
		return codmun;
	}

	public void setCodmun(Long codmun) {
		this.codmun = codmun;
	}

	public Long getCoddep() {
		return coddep;
	}

	public void setCoddep(Long coddep) {
		this.coddep = coddep;
	}

	public DepartamentoModel getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoModel departamento) {
		this.departamento = departamento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
