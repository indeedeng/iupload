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

/**
 * Created by akira on 5/13/14.
 */
public interface FileSystemProxy {
    public List<String> listFiles(String path) throws IOException;
    public List<String> listDirs(String path) throws IOException;
    public void createDir(String path, String name) throws IOException;
    public void createFile(String path, String name, InputStream inputStream) throws IOException;
    public void remove(String path) throws IOException;
    public InputStream getInputStream(String path) throws IOException;
    public void mkdirs(String rootPath) throws IOException;
    public String getName(String path) throws IOException;
    public long getSize(String path) throws IOException;
    public Date getLastModifiedAt(String path) throws IOException;
}
