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
 package com.indeed.iupload.core.authentification;

import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author akira
 */
public class UserPermission {

    private final boolean isRoot;
    private final Set<String> writableRepositories;
    private final Map<String, Set<String>> writableIndexes;

    public UserPermission(boolean isRoot, Set<String> writableRepositories, Map<String, Set<String>> writableIndexes) {
        this.isRoot = isRoot;
        this.writableRepositories = writableRepositories;
        this.writableIndexes = writableIndexes;
    }

    public boolean isWritable(String repositoryName) {
        return isRoot || writableRepositories.contains(repositoryName);
    }

    public boolean isWritable(String repositoryName, String indexName) {
        return isWritable(repositoryName) ||
                writableIndexes.containsKey(repositoryName)
                && writableIndexes.get(repositoryName).contains(indexName);
    }

    @JsonProperty
    @Deprecated
    public boolean hasRootAccess() {
        return isRoot;
    }

    @JsonProperty
    public boolean isRootAccess() {
        return isRoot;
    }

    @JsonProperty
    public Set<String> getWritableRepositories() {
        return writableRepositories;
    }

    @JsonProperty
    public Map<String, Set<String>> getWritableIndexes() {
        return writableIndexes;
    }
}
