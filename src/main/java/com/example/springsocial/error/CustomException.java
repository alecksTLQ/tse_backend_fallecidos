/**
 * 
 */
package com.example.springsocial.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;

/**
 * @author jorge
 *
 */
@SuppressWarnings("serial")
public class CustomException extends Throwable  {
	private int code;
	private String title;
	private String className;
	private int lineNumber;
	private List<ErrorMessage> messageList;
	private StackTraceElement[] stackTrace;
	
	public CustomException(String message, Throwable cause, ErrorCode errorCode,String className) {
		super(message, cause);
		this.setClassName(className);
		this.setCode(errorCode.get_code());
		this.setTitle(errorCode.get_title());
		this.setStackTrace(cause.getStackTrace());
		this.setLineNumber(cause.getStackTrace()[0].getLineNumber());
		this.setMessageList(getMessageList(cause));
	}
	
	public CustomException(String message, Throwable cause, ErrorCode errorCode,String className,List<ErrorMessage> messageList) {
		super(message, cause);
		this.setClassName(className);
		this.setCode(errorCode.get_code());
		this.setTitle(errorCode.get_title());
		this.setStackTrace(cause.getStackTrace());
		if (cause.getStackTrace()!=null)
			this.setLineNumber(cause.getStackTrace()[0].getLineNumber());
		else this.setLineNumber(0);
		this.setMessageList(messageList);
	}
	
	public CustomException(CustomException exception) {
		super(exception.getMessage(), exception.getCause());
		this.setClassName(exception.getClassName());
		this.setCode(exception.getCode());
		this.setTitle(exception.getTitle());
		this.setStackTrace(exception.getStackTrace());
		this.setLineNumber(exception.getStackTrace()[0].getLineNumber());
		this.setMessageList(getMessageList(exception));
	}

	public CustomException(String message,  ErrorCode errorCode,String className, int lineNumber) {
		super(message);
		this.setClassName(className);
		this.setCode(errorCode.get_code());
		this.setTitle(errorCode.get_title());
		this.setStackTrace(null);
		this.setLineNumber(lineNumber);
		this.setMessageList(null);
	}
	
	public CustomException(String message,  ErrorCode errorCode,String className,  List<ErrorMessage> errorMessageList) {
		super(message);
		this.setClassName(className);
		this.setCode(errorCode.get_code());
		this.setTitle(errorCode.get_title());
		this.setStackTrace(null);
		this.setLineNumber(0);
		this.setMessageList(errorMessageList);
	}
	
	 
	public CustomException(String message,  ErrorCode errorCode,String className, int lineNumber, Set<ConstraintViolation<Object>> errors) {
		super(message);
		this.setClassName(className);
		this.setCode(errorCode.get_code());
		this.setTitle(errorCode.get_title());
		this.setStackTrace(null);
		this.setLineNumber(lineNumber);
		List<ErrorMessage> errorMessageList =  new ArrayList<ErrorMessage>();
		for (ConstraintViolation<Object> constraintViolation : errors) {
			 ErrorMessage messageElement = new ErrorMessage();
			 messageElement.setMessage(constraintViolation.getMessage());
			 messageElement.setMethodName(constraintViolation.getPropertyPath().toString());
			 errorMessageList.add(messageElement);
		}
		this.setMessageList(errorMessageList);
	}

	public List<ErrorMessage> getMessageList(Throwable throwable) {
		List<ErrorMessage> errorMessageList =  new ArrayList<ErrorMessage>();
	    while (throwable != null) {
	    	ErrorMessage message = new ErrorMessage();
	    	message.setMessage( throwable.getMessage());
	    	message.setLine(throwable.getStackTrace()[0].getLineNumber());
	    	message.setMethodName(throwable.getStackTrace()[0].getMethodName());
	    	message.setFileName(throwable.getStackTrace()[0].getFileName() );
	    	message.setClassName(throwable.getStackTrace()[0].getClassName());
	    	errorMessageList.add(message);
	    	throwable = throwable.getCause();
	    }
	    return errorMessageList; 
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int _code) {
		this.code = _code;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String _className) {
		this.className = _className;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String _title) {
		this.title = _title;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(StackTraceElement[] _stackTrace) {
		this.stackTrace = _stackTrace;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int _lineNumber) {
		this.lineNumber = _lineNumber;
	}

	public List<ErrorMessage> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<ErrorMessage> _messageList) {
		this.messageList = _messageList;
	}

	
}
