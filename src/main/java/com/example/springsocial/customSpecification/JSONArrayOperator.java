package com.example.springsocial.customSpecification;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONArrayOperator{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private JSONArray response;
	private JSONArray arrayValue;
	private String operator;
	
	JSONArrayOperator(){}
	
	// SET
	public void setArrayValue(JSONArray arrayValue) { this.arrayValue=arrayValue;}
	public void setOperator(String operator) {this.operator= operator;};
	
	// GET
	public JSONArray get() {
		response= new JSONArray();
		setResponse();
		return this.response;
	}
	
	// PRIVATE
	private void setResponse() {
		JSONObject filterElement;
		try {
			if (arrayValue==null) return;
			for(int index=0; index < this.arrayValue.length(); index++) {
				if (!this.arrayValue.get(0).equals(null)) {
					filterElement = (JSONObject) this.arrayValue.get(index);
					if (filterElement.has("operator")){
						if (filterElement.get("operator").equals(operator)) {
							this.response.put(filterElement);
						}	
					}	
				}
				
			}	
		}catch(Exception ex) {
			logger.error(ex.toString());
		}
	}
	
}
