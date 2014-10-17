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

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.indeed.util.core.Throwables2;
import com.indeed.util.core.io.Closeables2;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author akira
 */
public class FileBasedUserPermissionProvider implements UserPermissionProvider {
    private static Logger logger = Logger.getLogger(FileBasedUserPermissionProvider.class);
    private Set<String> rootUserNames;
    private Set<String> anyoneWritableRepositories;
    private Map<String, Map<String, Set<String>>> indexwiseAllowedUsers;
    private UserPermissionConfigLoader userPermissionConfigLoader;

    public FileBasedUserPermissionProvider(UserPermissionConfigLoader loader) throws IOException {
        this.userPermissionConfigLoader = loader;
        reload();
    }

    protected static String getContent(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charsets.UTF_8));
        try {
            String content = "";
            String line;
            while ((line = reader.readLine()) != null) {
                content += line + System.getProperty("line.separator");
            }
            return content;
        } catch (Throwable t) {
            throw Throwables2.propagate(t, IOException.class);
        } finally {
            reader.close();
        }
    }

    protected static Set<String> getStringSetField(DBObject node, String fieldName) {
        if (node.get(fieldName) == null || !(node.get(fieldName) instanceof BasicDBList)) {
            return null;
        }
        BasicDBList list = (BasicDBList) node.get(fieldName);
        return Sets.newHashSet(Collections2.transform(list, new Function<Object, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Object input) {
                if (input == null) {
                    return null;
                }
                return input.toString();
            }
        }));
    }

    @Override
    @Scheduled(cron = "* */5 * * * *")
    public void reload() throws IOException {
        InputStream is = userPermissionConfigLoader.getConfigJsonStream();
        try {
            DBObject configRoot = (DBObject) JSON.parse(getContent(is));
            setUp(configRoot);
        } finally {
            Closeables2.closeQuietly(is, logger);
        }
        logger.info("Reloaded auth configuration.");
    }

    protected void setUp(DBObject configRoot) {
        this.rootUserNames = getStringSetField(configRoot, "rootUserNames");
        this.anyoneWritableRepositories = getStringSetField(configRoot, "anyoneWritableRepositories");
        this.indexwiseAllowedUsers = new HashMap<String, Map<String, Set<String>>>();

        final DBObject repositoryIndexMap = (DBObject)configRoot.get("indexwiseAllowedUsers");
        for (String repositoryName : repositoryIndexMap.keySet()) {
            final DBObject indexUsersMap = (DBObject) repositoryIndexMap.get(repositoryName);
            final Map<String, Set<String>> map = new HashMap<String, Set<String>>();
            for (String indexName : indexUsersMap.keySet()) {
                map.put(indexName, getStringSetField(indexUsersMap, indexName));
            }
            this.indexwiseAllowedUsers.put(repositoryName, map);
        }
    }

    @Override
    public UserPermission provideUserPermission(User user) {
        boolean isRoot = !Strings.isNullOrEmpty(user.getName())
                && rootUserNames.contains(user.getName());
        Map<String, Set<String>> writableIndexes = new HashMap<String, Set<String>>();
        for (String repositoryName : indexwiseAllowedUsers.keySet()) {
            Map<String, Set<String>> map = indexwiseAllowedUsers.get(repositoryName);
            writableIndexes.put(repositoryName, new HashSet<String>());
            for (String indexName : map.keySet()) {
                if (map.get(indexName).contains(user.getName())) {
                    writableIndexes.get(repositoryName).add(indexName);
                }
            }
        }
        return new UserPermission(isRoot, anyoneWritableRepositories, writableIndexes);
    }
}
