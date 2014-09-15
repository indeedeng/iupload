package com.indeed.iupload.core.authentification;

import java.io.IOException;

import org.springframework.scheduling.annotation.Scheduled;

public interface UserPermissionProvider {

    public void reload() throws IOException;

    public UserPermission provideUserPermission(User user);

}