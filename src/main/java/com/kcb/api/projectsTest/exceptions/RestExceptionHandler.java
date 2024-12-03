package com.kcb.api.projectsTest.exceptions;

import com.kcb.api.projectsTest.dtos.ErrorResp;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author martin
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value   = {SystemException.class })
    public ResponseEntity<Object> handleSystemExceptions(SystemException ex, WebRequest request) {
        return switch (ex.getErrorCode()) {
            case BAD_REQ -> errorResp("Data Validation Error", HttpStatus.BAD_REQUEST,"3333");
            default -> errorResp("JSON Syntax Error", HttpStatus.INTERNAL_SERVER_ERROR,"2222");
        };
    }
    
    @ExceptionHandler(value   = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleInvalidInputsExceptions(RuntimeException ex, WebRequest request) {
        return errorResp("Data Validation Error : "+ex.getMessage(), HttpStatus.BAD_REQUEST,"3333");
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errors = ex.getBindingResult().getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(", "));
        return errorResp("Data Validation Error : "+errors, HttpStatus.BAD_REQUEST,"3333");
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return errorResp("JSON Syntax Error", HttpStatus.INTERNAL_SERVER_ERROR,"2222");
    }
    
    private ResponseEntity errorResp(String errorMessage, HttpStatus httpStatus,String statusCode) {
        return new ResponseEntity(new ErrorResp(statusCode, errorMessage), httpStatus);
    }
}