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
@SuppressWarnings({"unchecked","null"})
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value   = {SystemException.class })
    public ResponseEntity<Object> handleSystemExceptions(SystemException ex, WebRequest request) {
        return switch (ex.getErrorCode()) {
            case BAD_REQ -> errorResp(ex.getMessage(), HttpStatus.BAD_REQUEST,"3333");
            case NOT_CONTENT -> errorResp(ex.getMessage(), HttpStatus.NO_CONTENT, "1111");
            default -> errorResp("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR,"2222");
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
        return errorResp("Data Validation Error : "+extractProblemPart(ex.getMessage()), HttpStatus.BAD_REQUEST,"3333");
    }
    
    private ResponseEntity errorResp(String errorMessage, HttpStatus httpStatus,String statusCode) {
        return new ResponseEntity(new ErrorResp(statusCode, errorMessage), httpStatus);
    }

    private String extractProblemPart(String fullMessage) {
        String problemPrefix = "problem: ";
        int problemIndex = fullMessage.indexOf(problemPrefix);
        if (problemIndex != -1) {
            return fullMessage.substring(problemIndex + problemPrefix.length());
        }
        return fullMessage; // Fallback to the full message if the problem part is not found
    }
}