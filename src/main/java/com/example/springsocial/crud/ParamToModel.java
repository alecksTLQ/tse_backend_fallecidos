package com.example.springsocial.crud;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.PropertyUtils;
import com.example.springsocial.error.CustomException;
import com.example.springsocial.error.ErrorCode;
import com.example.springsocial.generic.GenericClass;
import com.example.springsocial.generic.StringTools;

@SuppressWarnings("rawtypes")
public class ParamToModel {
	private GenericClass genericClass;
	private Object paramObject, elementObject;
	private StringTools stringTools= new StringTools();
	
	public Object getElementObject() {
		return elementObject;
	}

	public void setElementObject(Object elementObject) {
		this.elementObject = elementObject;
	}

	public Object getParamObject() {
		return paramObject;
	}

	public void setParamObject(Object paramObject) {
		this.paramObject = paramObject;
	}
	
	public void setParamsToElement() throws CustomException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String paramName="";
		Object paramValue;
		
		for(Field param: this.paramObject.getClass().getDeclaredFields()){
			paramName=param.getName();
			if (!paramName.equals("format")){
				paramValue = getValueOfParamObject(paramName);
				setValueToElementObject(paramName, paramValue);
			}				
		}
	}
	
	private Object getValueOfParamObject(String name) throws CustomException {
		stringTools.setValue(name);
		stringTools.capitalize();
		String methodName="get"+stringTools.getValue();
		genericClass= new GenericClass(this.paramObject,methodName);
		genericClass.executeMethod();
		if (genericClass.getIsError()==true)
			throw new CustomException("Error en la asignación de datos para crear un elemento",ErrorCode.REST_CREATE, this.getClass().getSimpleName().toString() , null);
		
		return genericClass.getResult();
	}
	
	private void setValueToElementObject(String name, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException  {
		if (name.equals("createdAt") || name.equals("id")){
			System.out.println("param name (ommit): "+name+", value "+value);
		}else{
			PropertyUtils.setProperty(this.elementObject, name, value);
		}
	}
	
	 
}
