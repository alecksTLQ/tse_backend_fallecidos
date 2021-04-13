package com.example.springsocial.model;

public class Persona {
	private String nombres,descripcion,nrocedula1,nrocedula2,nomDepto,nomMupio,cui,apecasada,nroboleta;
	private Long codstatus;
	
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNrocedula1() {
		return nrocedula1;
	}
	public void setNrocedula1(String nrocedula1) {
		this.nrocedula1 = nrocedula1;
	}
	public String getNrocedula2() {
		return nrocedula2;
	}
	public void setNrocedula2(String nrocedula2) {
		this.nrocedula2 = nrocedula2;
	}
	public String getNomDepto() {
		return nomDepto;
	}
	public void setNomDepto(String nomDepto) {
		this.nomDepto = nomDepto;
	}
	public String getNomMupio() {
		return nomMupio;
	}
	public void setNomMupio(String nomMupio) {
		this.nomMupio = nomMupio;
	}
	public String getCui() {
		return cui;
	}
	public void setCui(String cui) {
		this.cui = cui;
	}
	public String getApecasada() {
		return apecasada;
	}
	public void setApecasada(String apecasada) {
		this.apecasada = apecasada;
	}
	public String getNroboleta() {
		return nroboleta;
	}
	public void setNroboleta(String nroboleta) {
		this.nroboleta = nroboleta;
	}
	
	public Long getCodstatus() {
		return codstatus;
	}
	public void setCodstatus(Long codstatus) {
		this.codstatus = codstatus;
	}
	
}