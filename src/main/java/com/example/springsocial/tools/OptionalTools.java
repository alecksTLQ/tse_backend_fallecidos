package com.example.springsocial.tools;

import java.util.Optional;

import org.codehaus.jettison.json.JSONArray;

public class OptionalTools {

	private Optional<String> value;
	private JSONArray jsonArray;
	
	public Optional<String> getValue() { return value; }
	public void setValue(Optional<String> value) {this.value = value;}
	
	public JSONArray getJsonArray() {return this.jsonArray;}
	
	public void convertJsonArray() {
		try {
			jsonArray = (value.isPresent() && !value.get().equals("undefined"))	?  new JSONArray(value.get()) : null;
		}catch(Exception ex) {
			jsonArray= null;
		}
	}
}
