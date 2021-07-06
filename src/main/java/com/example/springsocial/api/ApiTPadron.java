package com.example.springsocial.api;


public class ApiTPadron extends Api{
	
	public ApiTPadron() {
		setApiUrl("http://localhost:5007");
	}
	
	public void setGetPath() {
		setPath("/ConsultarDatosDeFallecidos/consultasPadron");
	}
	
}
