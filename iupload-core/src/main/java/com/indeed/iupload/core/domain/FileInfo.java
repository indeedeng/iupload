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
 package com.indeed.iupload.core.domain;

import com.indeed.iupload.core.filesystem.FileSystemProxy;
import com.indeed.iupload.core.filesystem.HDFSProxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by akira on 5/13/14.
 */

public class FileInfo {

    private FileSystemProxy fileSystemProxy;

    private String path;
    private String name;
    private long size;



    private String status;
    private Date lastModifiedAt;

    public FileInfo(String path, String status) throws IOException {
        fileSystemProxy = new HDFSProxy();
        setPath(path);
        setName(fileSystemProxy.getName(path));
        setSize(fileSystemProxy.getSize(path));
        setLastModifiedAt(fileSystemProxy.getLastModifiedAt(path));
        setStatus(status);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @JsonIgnore
    public InputStream getInputStream() throws IOException {
        return fileSystemProxy.getInputStream(this.getPath());
    }
}
