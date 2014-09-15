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
