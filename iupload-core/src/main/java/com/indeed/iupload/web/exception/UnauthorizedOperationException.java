package com.indeed.iupload.web.exception;

/**
 * Created by akira on 5/16/14.
 */
public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException() {
        super();
    }

    public UnauthorizedOperationException(String message) {
        super(message);
    }
}
