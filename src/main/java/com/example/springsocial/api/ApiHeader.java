package com.example.springsocial.api;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

public class ApiHeader {
	private HttpServletRequest request;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public String getAuthorizationHeader() {
		return getHeader("authorization");
	}
	
	public String getHeader(String header) {
    	HttpServletRequest httpRequest = (HttpServletRequest) this.request;

    	Map<String, String> headers = Collections
    	    .list(httpRequest.getHeaderNames())
    	    .stream()
    	    .collect(Collectors.toMap(h -> h, httpRequest::getHeader));	
    	
    	return headers.get("authorization");
    }

}
