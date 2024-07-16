package com.onevoker.blogapi.exceptions;

import org.springframework.http.HttpStatusCode;

public abstract class ApiException extends RuntimeException {
    protected ApiException(String message) {
        super(message);
    }

    public abstract HttpStatusCode getStatusCode();
}
