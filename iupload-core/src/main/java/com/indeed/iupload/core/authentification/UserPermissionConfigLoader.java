package com.indeed.iupload.core.authentification;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author akira
 */
public interface UserPermissionConfigLoader {
    public InputStream getConfigJsonStream() throws IOException;
}
