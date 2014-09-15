package com.indeed.iupload.core.authentification;

import com.indeed.iupload.core.filesystem.FileSystemProxy;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by akira on 7/10/14.
 */
public class FileSystemUserPermissionConfigLoader implements UserPermissionConfigLoader {
    private FileSystemProxy fileSystemProxy;

    public FileSystemUserPermissionConfigLoader(FileSystemProxy fileSystemProxy) {
        this.fileSystemProxy = fileSystemProxy;
    }

    @Override
    public InputStream getConfigJsonStream() throws IOException {
        return fileSystemProxy.getInputStream("iupload.auth.json");
    }
}
