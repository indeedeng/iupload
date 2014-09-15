package com.indeed.iupload.web.response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akira on 5/12/14.
 */
public class BasicResponse {
    public final boolean success;
    public final Map<String, Object> errors;
    public final Map<String, Object> body;

    private BasicResponse(
            boolean success,
            Map<String, Object> errors,
            Map<String, Object> body
    ) {
        this.success = success;
        this.errors = errors;
        this.body = body;
    }

    public static BasicResponse success(Map<String, Object> body) {
        return new BasicResponse(true, null, body);
    }

    public static BasicResponse error(String message) {
        HashMap<String, Object> errors = new HashMap<String, Object>();
        errors.put("message", message);
        return new BasicResponse(false, errors, null);
    }
}
