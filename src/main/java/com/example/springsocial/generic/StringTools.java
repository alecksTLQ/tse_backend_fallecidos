package com.example.springsocial.generic;

public class StringTools {
	private String value;

	public void capitalize() {
		if (this.value.length()==0) this.value="";
		this.value= this.value.substring(0, 1).toUpperCase() + this.value.substring(1);
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
