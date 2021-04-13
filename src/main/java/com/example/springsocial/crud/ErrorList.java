package com.example.springsocial.crud;

import java.util.ArrayList;
import java.util.List;
import com.example.springsocial.error.ErrorMessage;

public class ErrorList{
	private List<ErrorMessage> list;
	
	public ErrorList() {
		list= new ArrayList<ErrorMessage>();
	}
	
	public void clear(){
		list= new ArrayList<ErrorMessage>();
	}
	
	public void add(String attribute, String message, String label) {
		ErrorMessage patternError = new ErrorMessage();
		patternError.setMessage(message);
		patternError.setAttribute(attribute);
		patternError.setLabel(label);
		list.add(patternError);
	}
	
	public Integer size() {
		return list.size();
	}
	
	public List<ErrorMessage> get(){
		return list;
	}
}
