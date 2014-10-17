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

/**
 * Created by akira on 5/14/14.
 */
public enum FileStatus {
    Indexing("indexing", "tsvtoindex"),
    Indexed("indexed", "indexedtsv"),
    Failed("failed", "failed");

    private String name;
    private String dirName;

    FileStatus(String name, String dirName) {
        this.name = name;
        this.dirName = dirName;
    }

    public String getName() {
        return name;
    }

    public String getDirName() {
        return dirName;
    }

    public static FileStatus fromName(String name) {
        if (name.equals("indexing")) {
            return Indexing;
        } else if (name.equals("indexed")) {
            return Indexed;
        } else {
            return Failed;
        }
    }
}
