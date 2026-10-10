package com.aa.constant;

public enum EnumTest {
//Created Date 27/07/2025 and Time 01:19:10
	
	
	// 2xx Success
    SUCCESS_OK(200, "OK",true),
    SUCCESS_CREATED(201, "Created",true),
    SUCCESS_NO_CONTENT(204, "No Content",true),

    // 4xx Client Errors
    CLIENT_ERROR_BAD_REQUEST(400, "Bad Request",false),
    CLIENT_ERROR_UNAUTHORIZED(401, "Unauthorized",false),
    CLIENT_ERROR_FORBIDDEN(403, "Forbidden",false),
    CLIENT_ERROR_NOT_FOUND(404, "Not Found",false),

	
	 // 5xx Server Errors
    SERVER_ERROR_INTERNAL_SERVER_ERROR(500, "Internal Server Error",false),
    SERVER_ERROR_NOT_IMPLEMENTED(501, "Not Implemented",false),
    SERVER_ERROR_BAD_GATEWAY(502, "Bad Gateway",false);

	private int statuscd;
	
	private String desc;
	
	private boolean isTrue;
	
	EnumTest(int a,String b,boolean c){
		this.statuscd=a;
		
		this.desc=b;
		
		this.isTrue=c;
		
		
	}
	
	public int returnStatusCode() {
		
		return statuscd;
	}
	
	public String returnDescription() {
		
		return desc;
	}
	
	public boolean isSuccuss() {
		
		
		return isTrue;
	}
}
