package com.example.springsocial.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@Entity
@Table(name="THISTORIALCIERRE")
@SequenceGenerator(name="default_gen", sequenceName="CIERRE", allocationSize = 1)
public class HistorialCierreModel implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator="default_gen")
	@Column(name="ID")
	private Long id;
	@Column(name="DESDE")
	private Date desde;
	@Column(name="HASTA")
	private Date hasta;
	@Column(name="LEIDOS")
	private Integer leidos;
	@Column(name="ACTUALIZADOS")
	private Integer actualizados;
	@Column(name="NOEMPADRONADOS")
	private Integer noempadronados;
	@Column(name="REGPROCESADOS")
	private Integer registrosprocesados;
	@Column(name="EMPNOACTUALIZADOS")
	private Integer empadronadosnoactualizados;
	@Column(name="USRPRO")
	private String usuario;
	@Column(name="MAQPRO")
	private String maqpro;
	@Column(name="FECPRO")
	private Date fechaproceso;
	
	
	public HistorialCierreModel() {
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Date getDesde() {
		return desde;
	}


	public void setDesde(Date desde) {
		this.desde = desde;
	}


	public Date getHasta() {
		return hasta;
	}


	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}


	public Integer getLeidos() {
		return leidos;
	}


	public void setLeidos(Integer leidos) {
		this.leidos = leidos;
	}


	public Integer getActualizados() {
		return actualizados;
	}


	public void setActualizados(Integer actualizados) {
		this.actualizados = actualizados;
	}


	public Integer getNoempadronados() {
		return noempadronados;
	}


	public void setNoempadronados(Integer noempadronados) {
		this.noempadronados = noempadronados;
	}


	public Integer getRegistrosprocesados() {
		return registrosprocesados;
	}


	public void setRegistrosprocesados(Integer registrosprocesados) {
		this.registrosprocesados = registrosprocesados;
	}


	public Integer getEmpadronadosnoactualizados() {
		return empadronadosnoactualizados;
	}


	public void setEmpadronadosnoactualizados(Integer empadronadosnoactualizados) {
		this.empadronadosnoactualizados = empadronadosnoactualizados;
	}


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public String getMaqpro() {
		return maqpro;
	}


	public void setMaqpro(String maqpro) {
		this.maqpro = maqpro;
	}


	public Date getFechaproceso() {
		return fechaproceso;
	}


	public void setFechaproceso(Date fechaproceso) {
		this.fechaproceso = fechaproceso;
	}

	
	
}
