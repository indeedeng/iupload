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
