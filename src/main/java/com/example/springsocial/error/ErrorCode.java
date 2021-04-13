package com.example.springsocial.error;

public enum ErrorCode {
	GENERIC_ERROR(100,"Generic exception has occurred"),
	APPLICATION_CONFIG(101,"Error when configuring the application"),
	ENTITI(102,"Entiti name doesnt exist for the module"),
	DATABASE_CONNECTION(200,"The connection to the database failed"),
	DATABASE_TABLE(201,"The connection to the database failed"),
	REST_QUERY(201,"Custom query"),
	REST_FIND(300,"Find registers"),
	REST_CREATE(301,"Create a registrer"),
	REST_UPDATE(302,"Update a registrer"),
	REST_DELETE(303,"Delete a registrer"),
	REST_VALIDATIONS(304,"Validations"),
	SAP_RFC(305,"SAP rfc"),
	REPORT(310,"Report"),
	ACCESS_DENIED(400,"Access denied");
	
	private final int _code;
	private final String _title;

	ErrorCode(int code, String title) {
		this._code = code;
	    this._title = title;
	}

	public int get_code() { return this._code; }
	public String get_title() { return this._title; }
	
	public static ErrorCode getByCode(Integer code) {
		switch ( code ) {
	    	case 100:
	    		return GENERIC_ERROR;
			case 101:
		    	return APPLICATION_CONFIG;
			case 102:
		        return ENTITI;
			case 200:
		        return DATABASE_CONNECTION;
			case 201:
				return DATABASE_TABLE;
			case 300:
				return REST_FIND;
			case 301:
				return REST_CREATE;
			case 302:
				return REST_UPDATE;
			case 303:
				return REST_DELETE;
			case 304:
				return REST_VALIDATIONS;
			case 305:
				return SAP_RFC;
			case 400:
				return ACCESS_DENIED;
			default:
		        return GENERIC_ERROR;
	    }
		
	}
}
