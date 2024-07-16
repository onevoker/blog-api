package com.onevoker.blogapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class DuplicateUsernameException extends ApiException {
    private static final String USERNAME_IN_USE_MESSAGE = "This username already in use, try another one";

    public DuplicateUsernameException() {
        super(USERNAME_IN_USE_MESSAGE);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.CONFLICT;
    }
}
