package com.example.springsocial.model;

public class ReportDeptoMunic {

	
	private String departamento;
	private String municipio;
	private String cancelados;
	private String investigacion;
	private String otros;
	private String total;
	
	
	
	public ReportDeptoMunic() {
	}
	
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getCancelados() {
		return cancelados;
	}
	public void setCancelados(String cancelados) {
		this.cancelados = cancelados;
	}
	public String getInvestigacion() {
		return investigacion;
	}
	public void setInvestigacion(String investigacion) {
		this.investigacion = investigacion;
	}
	public String getOtros() {
		return otros;
	}
	public void setOtros(String otros) {
		this.otros = otros;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	
}
