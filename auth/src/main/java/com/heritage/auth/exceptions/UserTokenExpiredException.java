package com.heritage.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserTokenExpiredException extends RuntimeException{
    public UserTokenExpiredException() {
        super();
    }

    public UserTokenExpiredException(String message) {
        super(message);
    }

    public UserTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
