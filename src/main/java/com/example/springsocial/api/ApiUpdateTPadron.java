package com.example.springsocial.api;

public class ApiUpdateTPadron extends Api{

	public ApiUpdateTPadron() {
		setApiUrl("http://localhost:5007");
	}
	
	public void setGetPath() {
		setPath("/ConsultarDatosDeFallecidos/updateTpadron");
	}
}
