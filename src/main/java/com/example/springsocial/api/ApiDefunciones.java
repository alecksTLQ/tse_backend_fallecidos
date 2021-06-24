package com.example.springsocial.api;

public class ApiDefunciones extends Api{
	
	public ApiDefunciones() {
		setApiUrl("http://192.168.79.66:5019");
	}
	
	public void setGetPath() {
		setPath("/Defunciones/obtenerDefuncionesDia");
	}
}
