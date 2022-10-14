package com.heritage.company.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CompanyAlreadyExistsException extends RuntimeException{
    public CompanyAlreadyExistsException() {
        super();
    }
    public CompanyAlreadyExistsException(String message) {
        super(message);
    }
}
