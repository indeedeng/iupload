/*
 * Copyright (C) 2014 Indeed Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
