package com.example.springsocial.tools;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.example.springsocial.crud.ObjectSetGet;

public class ApiResponseGetValuesTools {
	private Object apiResponse;
	private String value, returnedValue;




	public void getJsonValue() throws JSONException, Exception {	
		ObjectSetGet responseObject = new ObjectSetGet();
		JSONObject jsonResponse = new JSONObject();
		responseObject.setObject(apiResponse);
		jsonResponse = new JSONObject(responseObject.getValue("data").toString());
		this.returnedValue = jsonResponse.getString(this.value).toString();
	}
	public String getResponse(Object data, String value) throws JSONException, Exception {	
		ObjectSetGet responseObject = new ObjectSetGet();
		JSONObject jsonResponse = new JSONObject();
		responseObject.setObject(apiResponse);
		jsonResponse = new JSONObject(responseObject.getValue("data").toString());
		String returnedValue = jsonResponse.getString(value).toString();

		
		return returnedValue;
	}
	public String getReturnedValue() {
		return returnedValue;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setApiResponse(Object apiResponse) {
		this.apiResponse = apiResponse;
	}
}
