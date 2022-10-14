package com.heritage.company.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class StockClientNotFoundException extends RuntimeException{

    public StockClientNotFoundException() {}
    public StockClientNotFoundException(String message) {super(message);}

}
