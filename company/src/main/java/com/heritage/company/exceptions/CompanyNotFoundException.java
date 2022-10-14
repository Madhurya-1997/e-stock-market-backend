package com.heritage.company.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CompanyNotFoundException  extends RuntimeException{
    public CompanyNotFoundException() {
        super();
    }
    public CompanyNotFoundException(String message) {
        super(message);
    }
}
