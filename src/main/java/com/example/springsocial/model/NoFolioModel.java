package com.example.springsocial.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name="TNROFOLIO")
//@SequenceGenerator(name = "default_gen", sequenceName = "folio", allocationSize = 1)
public class NoFolioModel implements Serializable{
	
	//@GeneratedValue(generator="default_gen") 
	@Id
	@Column(name="NROFOLIO")
	private Integer nrofolio;
	@Column(name="AÑO")
	private Integer año;
	@Column(name="NROLINEA")
	private Integer nrolinea;
	@Column(name="ESTADO")
	private Integer estado;
	@Column(name="USUARIO")
	private String usuario;
	@Column(name="FECHAASIGNACION")
	private Date fechaasignacion;
	@Column(name="ESTADOREGISTRO")
	private Integer estadoregistro;
	
	
	
	
	public Integer getEstadoregistro() {
		return estadoregistro;
	}

	public void setEstadoregistro(Integer estadoregistro) {
		this.estadoregistro = estadoregistro;
	}

	public Integer getNrofolio() {
		return nrofolio;
	}
	public void setNrofolio(Integer nrofolio) {
		this.nrofolio = nrofolio;
	}
	public Integer getAño() {
		return año;
	}
	public void setAño(Integer año) {
		this.año = año;
	}
	public Integer getNrolinea() {
		return nrolinea;
	}
	public void setNrolinea(Integer nrolinea) {
		this.nrolinea = nrolinea;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public Date getFechaasignacion() {
		return fechaasignacion;
	}
	public void setFechaasignacion(Date fechaasignacion) {
		this.fechaasignacion = fechaasignacion;
	}

	public NoFolioModel() {
		this.getNrofolio();
	}
	
}
