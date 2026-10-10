package com.aa.constant;

import java.util.HashMap;
import java.util.Map;

public enum StatusDesc {

    // 2xx Success
    SUCCESS_OK(200, "OK"),
    SUCCESS_CREATED(201, "Created"),
    SUCCESS_NO_CONTENT(204, "No Content"),

    // 4xx Client Errors
    CLIENT_ERROR_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_UNAUTHORIZED(401, "Unauthorized"),
    CLIENT_ERROR_FORBIDDEN(403, "Forbidden"),
    CLIENT_ERROR_NOT_FOUND(404, "Not Found"),

    // 5xx Server Errors
    SERVER_ERROR_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVER_ERROR_NOT_IMPLEMENTED(501, "Not Implemented"),
    SERVER_ERROR_BAD_GATEWAY(502, "Bad Gateway");

    private final int code;
    private final String reasonPhrase;

    // Static map for reverse lookup
    private static final Map<Integer, StatusDesc> codeToStatusMap = new HashMap<>();

    // Populate the map
    static {
        for (StatusDesc statusCode : values()) {
            codeToStatusMap.put(statusCode.code, statusCode);
        }
    }

    StatusDesc(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public int getCode() {
        return code;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    // Reverse lookup by code
    public static StatusDesc fromCode(int code) {
        return codeToStatusMap.get(code);
    }

    @Override
    public String toString() {
        return code + " " + reasonPhrase;
    }
}
