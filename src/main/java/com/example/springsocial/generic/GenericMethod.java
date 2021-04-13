package com.example.springsocial.generic;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@SuppressWarnings("rawtypes")
public class GenericMethod<T> {
	private Method method;
	private Boolean isError=false;
	private String errorMessage;
	private T param;
	private String name;
	private Object genericClass;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public GenericMethod(Object _class, String _name){
		this.genericClass=_class;
		this.name=_name;
	}
	
	public Method getMethod() {
		return this.method;
	}
	
	public void setMethod() {
		try{
			if (param == null){
				method = genericClass.getClass().getMethod(this.name);
			}else if (this.name=="findById"){
				method = genericClass.getClass().getMethod(this.name,Object.class);
			}else if (param instanceof Integer){
				method = genericClass.getClass().getMethod(this.name,int.class);
			}else if (param instanceof Long){
				method = genericClass.getClass().getMethod(this.name,Long.class);
			}else if (param instanceof String){
				method = genericClass.getClass().getMethod(this.name,String.class);
			}else if (param instanceof Boolean){
				method = genericClass.getClass().getMethod(this.name,Boolean.class);	
			}else if (param instanceof Date){
				method = genericClass.getClass().getMethod(this.name,Date.class);	
			}else if (param instanceof Object[]){
				Class paramList [] = convertParamsToClass(param);
				method = genericClass.getClass().getMethod(this.name,paramList);
			}else if (param instanceof java.util.Optional[]){
				method = genericClass.getClass().getMethod(this.name,(Class<?>[]) param);
			}else if (param instanceof Set<?>){
				method = genericClass.getClass().getMethod(this.name,Set.class);
			}else if (param instanceof java.util.ArrayList){
				method = genericClass.getClass().getMethod(this.name,java.util.ArrayList.class);
			}else if (param instanceof Specification) {
				method = genericClass.getClass().getMethod(this.name,Specification.class);	
			}else if (param instanceof Object){
				if (this.name=="update" || this.name=="save" || this.name=="delete"){
					method = genericClass.getClass().getMethod(this.name,Object.class);
				}else{
					method = genericClass.getClass().getMethod(this.name,param.getClass());
				}
			}
		}catch(Exception exception){
			logger.error(exception.getMessage());
			this.method = null;
			this.setIsError(true);
			this.setErrorMessage("Error to execute method "+this.name+" in class "+this.genericClass.getClass().getName()+" with param "+this.param);
		}
	}
	
	public Class[]  convertParamsToClass(T  paramList) {
		Object [] paramListObjectArray = (Object [])paramList;
		Class[] params = new Class[paramListObjectArray.length];
		int index=0;
		for(Object param: paramListObjectArray){
			addParam(params,param,index);
			index++;
		}
		return params;
	}

	public void addParam(Class[] params, Object param, int index){
		if (param instanceof java.lang.Integer){
			params[index]=java.lang.Integer.class;
		}else if (param instanceof String){
			params[index]=String.class;
		}else if (param instanceof Long){
			params[index]=Long.class;
		}else if (param instanceof Date){
			params[index]=Date.class;
		}else if (param instanceof Specification) {
			params[index]=Specification.class;
		}else if (param instanceof PageRequest) {
			params[index]=Pageable.class;
		}else if (param instanceof java.util.Optional) {
			params[index]=java.util.Optional.class;
		}else {
			params[index]=Object.class;
		}
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
	public T getParam() {
		return param;
	}
	public void setParam(T param) {
		this.param = param;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
