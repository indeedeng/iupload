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
    public boolean hasRootAccess() {
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
