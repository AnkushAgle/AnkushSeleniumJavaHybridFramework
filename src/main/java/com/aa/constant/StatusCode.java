package com.aa.constant;

public enum StatusCode {

    SUCCESS_OK(200),
    SUCCESS_CREATED(201),
    SUCCESS_NO_CONTENT(204),
    CLIENT_ERROR_BAD_REQUEST(400),
    CLIENT_ERROR_UNAUTHORIZED(401),
    CLIENT_ERROR_FORBIDDEN(403), // Fixed typo: FORBIDDENT → FORBIDDEN
    SERVER_ERROR_INTERNAL_SERVER_ERROR(500);

    private final int status;

    StatusCode(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
