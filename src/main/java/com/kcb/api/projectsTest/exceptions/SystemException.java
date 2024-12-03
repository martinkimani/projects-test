package com.kcb.api.projectsTest.exceptions;

/**
 *
 * @author martins
 */
public class SystemException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public SystemException(String message, ErrorCode code){
        super(message);
        errorCode = code;
    }

    public SystemException(String message){
        super(message);
        errorCode = ErrorCode.UNEXPECTED;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
