package com.heritage.stock.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void  springHandleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void  springMethodArgHandleIssue(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Arguments mismatched...");
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public void  springCompanyHandleIssue(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.NOT_FOUND.value(), "Company not found");
    }

    @ExceptionHandler(CompanyAlreadyExistsException.class)
    public void  springUserExistsHandleIssue(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Company code already exists");
    }

    @ExceptionHandler(NotAllowedException.class)
    public void  springNotAllowedHandleIssue(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.FORBIDDEN.value(), "Cannot access this page");
    }

    @ExceptionHandler(CompanyCodeMismatchException.class)
    public void  springCompanyCodeMismatchAllowedHandleIssue(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.FORBIDDEN.value(), "Company codes mismatch !!");
    }
}
