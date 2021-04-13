package com.example.springsocial.crud;

import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.generic.GenericClass;
import com.example.springsocial.generic.StringTools;

@SuppressWarnings({"unchecked","rawtypes"})
public class ModelSetGet<T> {
	private Object model;
	private GenericClass genericClass;
	private StringTools stringTools = new StringTools();
	private String methodName;
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}
	
	
	public void setValue(String attribute, String description, Object value) throws CustomException{
		setMethodNameWithCapitalize(attribute,true);
		genericClass= new GenericClass(this.model,this.methodName,(T) value);
		
		genericClass.executeMethod();
		if (genericClass.getIsError()==true)
			throw new CustomException("Error al asignar "+description,ErrorCode.REST_CREATE, this.getClass().getSimpleName().toString() , null);
	}
	
	private void setMethodNameWithCapitalize(String attribute,Boolean isSet) {
		stringTools.setValue(attribute);
		stringTools.capitalize();
		if (isSet) 	this.methodName="set"+stringTools.getValue();	
		else 		this.methodName="get"+stringTools.getValue();	
	}
	
	public Object getValue(String attribute, String description) throws CustomException {
		setMethodNameWithCapitalize(attribute,false);
		genericClass = new GenericClass(this.model,this.methodName);
		genericClass.executeMethod();
		if (genericClass.getIsError()==true){
			throw new CustomException("Error al obtener "+description,ErrorCode.REST_CREATE, this.getClass().getSimpleName().toString() , null);
		}	
		return genericClass.getResult();
	}
	
}
