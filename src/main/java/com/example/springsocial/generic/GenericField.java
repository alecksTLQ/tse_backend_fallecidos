package com.example.springsocial.generic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericField<T> {
	private Boolean isError=false;
	private String errorMessage;
	private Object genericClass;
	private String attribute;
	private Object value;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	public GenericField(Object _genericClass){
		this.setGenericClass(_genericClass);
	}
	
	public Boolean getIsError() {
		return isError;
	}

	public void setIsError(Boolean isError) {
		this.isError = isError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public Object getGenericClass() {
		return genericClass;
	}


	public void setGenericClass(Object genericClass) {
		this.genericClass = genericClass;
	}


	public Object getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute=attribute;
	     
	}

	public Object getValue() {
		try{
			java.lang.reflect.Field field = this.genericClass.getClass().getDeclaredField(this.attribute);
		     field.setAccessible(true);
		     this.value= field.get(this.genericClass);
		}catch(Exception exception){
			logger.error(exception.getMessage());
			this.value=null;
			this.setIsError(true);
			this.setErrorMessage("Error to set "+this.attribute+" attribute");
		}
		
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	
}
