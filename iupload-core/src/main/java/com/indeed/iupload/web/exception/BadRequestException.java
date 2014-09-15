package com.indeed.iupload.web.exception;

/**
 * @author akira
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
