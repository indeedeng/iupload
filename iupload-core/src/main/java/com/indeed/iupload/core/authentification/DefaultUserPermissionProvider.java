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
 package com.indeed.iupload.core.authentification;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DefaultUserPermissionProvider implements UserPermissionProvider {
    private UserPermission defaultUser;

    public DefaultUserPermissionProvider() {
        this.defaultUser = new UserPermission(true, 
                                              new HashSet<String>(), 
                                              new HashMap<String,Set<String>>());
    }

    @Override
    public void reload() throws IOException {
        // nothing to do
    }

    @Override
    public UserPermission provideUserPermission(User user) {
        return defaultUser;
    }

}
