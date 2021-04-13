package com.example.springsocial.api;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.example.springsocial.tools.RestResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class Api {
	private String apiUrl;
	private String authorizationHeader = "";
	private String path ="";
	private String result ="";
	private JSONObject params;
	private Boolean headerIsFormData;
    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    // TEST THE API
    public static void main(String[] args) throws Exception {
    	Api obj = new Api();
        try {
            System.out.println("Testing 1 - Send Http GET request");
            obj.setPath("user/me");
            obj.setAuthorizationHeader("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTczNTkzNzIwLCJleHAiOjE1NzQ0NTc3MjB9.HGgxbtAn4Bzvi-xPtMfO7LVy80c3I4f92uN5rpsn7L8ghKjGu87kiorJjaz1eA9NSyHWG8wl0RFztVWUx9VWBw");
            obj.sendGet();
            
            obj.sendPost();
        } finally {
            obj.close();
        }
    }

    public Api() {
    	this.apiUrl="http://192.168.79.67:9763"; 
    	if  (!this.apiUrl.endsWith("/")) this.apiUrl+="/";
    	this.params=new JSONObject();
    	this.headerIsFormData=false;
    	System.out.println("ApiUrl "+ this.apiUrl);
    }
    
    private void close() throws IOException {
        httpClient.close();
    }

    public void setAuthorizationHeader(String authorizationHeader) {
    	this.authorizationHeader=authorizationHeader;
    }
    
    public void setPath(String path) {
    	this.path= path;
    }
    
    public void sendGet()  throws Exception {
        HttpGet request = new HttpGet(this.apiUrl + this.path);
        if (this.authorizationHeader!="")  request.addHeader("authorization",this.authorizationHeader);
        
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            System.out.println("status response: "+response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            //Header headers = entity.getContentType();
            if (entity != null) {	result = EntityUtils.toString(entity); 	}
        }catch(Exception ex) {
        	throw ex;
        }
    }
    
    public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getString () {
    	return result;
    } 
    
    public JSONObject getJSONObject() throws JSONException {
    	return  new JSONObject(this.result);
    }
    
	public RestResponse getRestResponse() {
    	return JSON.parseObject(this.result,RestResponse.class);
    }
   
	
    public void sendPost() throws Exception {
        HttpPost request = new HttpPost(this.apiUrl + this.path);
        if (this.authorizationHeader!="")  request.addHeader("authorization",this.authorizationHeader);
        
        if (this.headerIsFormData) {
        	request.addHeader("content-type", "application/x-www-form-urlencoded");
        	List<NameValuePair> paramsListAsPair = new ArrayList<NameValuePair>();
        	String key, value;
        	for (int index=0; index<this.params.names().length();index++) {
        		key = this.params.names().getString(index);
        		value  = this.params.getString(key);
        		paramsListAsPair.add(new BasicNameValuePair(key, value));	
        	}
        	request.setEntity(new UrlEncodedFormEntity(paramsListAsPair, "UTF-8"));
        }else {
        	request.addHeader("content-type", "application/json;charset=UTF-8");
        	request.setEntity(new StringEntity(this.params.toString(), Charset.forName("UTF-8")));	
        }
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
        	result = EntityUtils.toString(response.getEntity());
        	System.out.println("result"+result);
        }
    }

    public void clearParms() {
    	this.params= new JSONObject();
    }
    
    public void addParam(String key, String value) throws JSONException {
    	this.params.put(key,value);
    }
    
	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}
	
	public Boolean getHeaderIsFormData() {
		return headerIsFormData;
	}

	public void setHeaderIsFormData(Boolean headerIsFormData) {
		this.headerIsFormData = headerIsFormData;
	}
}
