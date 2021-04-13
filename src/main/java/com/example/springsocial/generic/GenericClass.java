package com.example.springsocial.generic;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unchecked","rawtypes"})
public class GenericClass<T> {
	private Boolean isError=false;
	private String errorMessage;
	private T result;
	private Object genericClass;
	private String methodName;
	private T param;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public GenericClass(Object _genericClass,String _methodName, T _param){
		this.genericClass=_genericClass;
		this.methodName=_methodName;
		this.param=_param;
	}
	
	public GenericClass(Object _genericClass,String _methodName){
		this.genericClass=_genericClass;
		this.methodName=_methodName;
		this.param=null;
	}
	
	public void executeMethod(){
		T result=null;
		GenericMethod genericMethod;
		try{
			genericMethod = new GenericMethod(genericClass,methodName);
			genericMethod.setParam(this.param);
			genericMethod.setMethod();	
			if (genericMethod.getIsError()==true){
				this.setIsError(true);
				this.setErrorMessage(genericMethod.getErrorMessage());
				return;
			}
			Method method = genericMethod.getMethod();
			
			if (this.param==null){
				result = (T) method.invoke(this.genericClass);	
			}else{
				if (this.param instanceof Object[]){
					result = (T) method.invoke(this.genericClass,getParamsOfMethod());
				}else{
					result = (T) method.invoke(this.genericClass,this.param);	
				}
			}
			this.setResult(result);
		}catch(Exception exception){
			logger.error(exception.getMessage());
			this.setIsError(true);
			this.setErrorMessage(exception.getCause().getMessage());
		}
	}
	
	private Object [] getParamsOfMethod(){
		Object [] result=null;
		if (this.param instanceof Object[]){
			Object [] paramsObjec = (Object []) this.param;
			result =new Object[paramsObjec.length];
			int index=0;
			for (Object obj: (Object[]) this.param){
				if ((obj instanceof Integer)==true){
					result[index]=new Integer((Integer) obj);
				}else  if ((obj instanceof String)==true){
					result[index]=new String((String) obj);
				}else  if ((obj instanceof Date)==true){
					result[index]=(Date) obj ;
				}else if ((obj instanceof Object)==true){
					result[index] = (Object)obj;
				}else if ((obj instanceof Long)==true){
					result[index] = (Long)obj;
				}else if ((obj instanceof Optional)==true){
					result[index] = (Optional<T>)obj;
				}
				index++;
			}
		}
		return result;
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

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Object getGenericClass() {
		return genericClass;
	}

	public void setGenericClass(Object genericClass) {
		this.genericClass = genericClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
}
