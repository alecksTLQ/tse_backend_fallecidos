package com.example.springsocial.crud;

import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

public class ObjectSetGet {
	private Object object;
	private ArrayList<String> exceptValues = new ArrayList<>();
	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	public Object getValue(String attribute)throws Exception{
		return PropertyUtils.getProperty(this.object, attribute);	
	}
	
	public void setValue(String attribute, Object value) throws Exception{
		if (!exceptValues.contains(attribute))
			PropertyUtils.setProperty(this.object, attribute, value);	
	}
	
	public boolean existAttribute(String attribute) {
		 return PropertyUtils.isReadable(this.object, attribute) && 
		           PropertyUtils.isWriteable(this.object, attribute); 
	}
	
	public void clearExceptValues() {
		exceptValues.clear();
	}
	
	public void addExceptValues(String attribute) {
		exceptValues.add(attribute);
	}
	
	public JSONObject convertAtJSON() throws JsonProcessingException {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(this.object);
		JSONObject convertObject = gson.fromJson(jsonStr, JSONObject.class);
		return convertObject;
	}
	
	public <T> T convertAtJSONType(Class<T> classOfT) throws JsonProcessingException {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(this.object);		 
		return gson.fromJson(jsonStr, classOfT);
	}
}
