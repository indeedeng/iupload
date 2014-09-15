package com.indeed.iupload.web.exception;

import java.io.IOException;

/**
 * Created by akira on 5/16/14.
 */
public class ResourceNotFoundException extends IOException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
