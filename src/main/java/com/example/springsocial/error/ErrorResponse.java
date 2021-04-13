/**
 * 
 */
package com.example.springsocial.error;

import java.util.List;

/**
 * @author jorge
 *
 */
public class ErrorResponse {
	private String className;
	private int code;
	private String title;
	private String message;
	private int lineNumber;
	private List<ErrorMessage> messageList;
	private StackTraceElement[]  stackTrace;

	public ErrorResponse(){}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public List<ErrorMessage> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<ErrorMessage> messageList) {
		this.messageList = messageList;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}
	

}
