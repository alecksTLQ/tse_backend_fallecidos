package com.example.springsocial.error;

public class ErrorFormat {
	ErrorResponse _errorResponse;
	
	public ErrorFormat(CustomException exception){
		_errorResponse=new ErrorResponse();
		_errorResponse.setClassName(exception.getClassName());
		_errorResponse.setCode(exception.getCode());
		_errorResponse.setTitle(exception.getTitle());
		_errorResponse.setMessage(exception.getMessage());
		_errorResponse.setStackTrace(exception.getStackTrace());
		_errorResponse.setLineNumber(exception.getLineNumber());
		_errorResponse.setMessageList(exception.getMessageList());
	}
	
	public ErrorResponse get_errorResponse(){
		return this._errorResponse;
	}
}