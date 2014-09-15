package com.indeed.iupload.web.config;

import com.indeed.iupload.core.authentification.DefaultUserPermissionProvider;
import com.indeed.iupload.core.authentification.FileSystemUserPermissionConfigLoader;
import com.indeed.iupload.core.authentification.FileBasedUserPermissionProvider;
import com.indeed.iupload.core.authentification.UserPermissionConfigLoader;
import com.indeed.iupload.core.authentification.UserPermissionProvider;
import com.indeed.iupload.core.filesystem.FileSystemProxy;
import com.indeed.iupload.core.filesystem.HDFSProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

/**
 * @author akira
 */
@Configuration
@ComponentScan(basePackages = "com.indeed.iupload")
@EnableScheduling
public class AppConfiguration {

    @Value("${permission.provider.use.default}")
    private String useDefaultPermissionProvider;

    @Bean
    public FileSystemProxy fileSystemProxy() {
        return new HDFSProxy();
    }

    @Bean
    @Autowired
    public UserPermissionProvider permissionProvider(FileSystemProxy fileSystemProxy) throws IOException {
        if ("true".equalsIgnoreCase(useDefaultPermissionProvider)) {
            return new DefaultUserPermissionProvider();
        } else {
            final UserPermissionConfigLoader loader;
            
            loader = new FileSystemUserPermissionConfigLoader(fileSystemProxy);
            return new FileBasedUserPermissionProvider(loader);
        }
    }
}
