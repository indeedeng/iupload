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
 package com.indeed.iupload.core.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class S3Proxy implements FileSystemProxy {

    @Override
    public List<String> listFiles(String path) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> listDirs(String path) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createDir(String path, String name) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void createFile(String path, String name, InputStream inputStream) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(String path) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void mkdirs(String rootPath) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public String getName(String path) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getSize(String path) throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Date getLastModifiedAt(String path) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
