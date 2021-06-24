package com.example.springsocial.api;

public class ApiRenapImages extends Api{
	
	
	public ApiRenapImages() {
		//setApiUrl(System.getenv("CERTIFICATE_SIGN_URL"));
		setApiUrl("http://192.168.79.66:5008");
	}
	
	public void setGetPath(String cui) {		setPath("/imagenesRenap/list/"+ cui);	}
}
