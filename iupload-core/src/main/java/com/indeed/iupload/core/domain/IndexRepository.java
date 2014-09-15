package com.indeed.iupload.core.domain;

import com.indeed.iupload.core.filesystem.FileSystemProxy;
import com.indeed.iupload.core.filesystem.HDFSProxy;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by akira on 5/13/14.
 */
@Configurable
public class IndexRepository {

    private final String name;
    private final String rootPath;

    private final FileSystemProxy fileSystemProxy;

    public IndexRepository(String name, String rootPath) throws IOException {
        this.rootPath = rootPath;
        this.name = name;
        fileSystemProxy = new HDFSProxy();
        for (FileStatus status : FileStatus.values()) {
            fileSystemProxy.mkdirs(getPathFor(status));
        }
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getName() {
        return this.name;
    }

    public String getPathFor(FileStatus status) {
        return getRootPath() + status.getDirName() + File.separator;
    }

    public String getIndexPathFor(FileStatus status, String indexName) {
        return getPathFor(status) + indexName;
    }

    private Set<String> getIndexNameList() throws IOException {
        Set<String> indexNames = new HashSet<String>();
        for (FileStatus status : FileStatus.values()) {
            for (String name : fileSystemProxy.listDirs(getPathFor(status))) {
                indexNames.add(name);
            }
        }
        return indexNames;
    }

    private Index getIndexObjectFromName(String name) throws IOException {
        return new Index(
                name,
                getIndexPathFor(FileStatus.Indexed, name),
                getIndexPathFor(FileStatus.Indexing, name),
                getIndexPathFor(FileStatus.Failed, name)
        );
    }

    public List<Index> getIndexList() throws IOException {
        List<Index> indexList = new ArrayList<Index>();
        for (String name : getIndexNameList()) {
            Index index = getIndexObjectFromName(name);
            indexList.add(index);
        }
        Collections.sort(indexList, new Comparator<Index>() {
            @Override
            public int compare(Index o1, Index o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return indexList;
    }

    public Index createIndex(String name) throws IOException {
        if (this.hasIndex(name)) {
            throw new IOException("The index '" + name + "'already exists.");
        }
        fileSystemProxy.createDir(getPathFor(FileStatus.Indexing), name);
        return getIndexObjectFromName(name);
    }

    public void deleteIndex(String name) throws IOException {
        Index index = this.find(name);
        if (index == null) {
            throw new IOException("No index named '" + name + "' found");
        }
        for (FileStatus status : FileStatus.values()) {
            fileSystemProxy.remove(getIndexPathFor(status, name));
        }
    }

    public boolean hasIndex(String name) throws IOException {
        return getIndexNameList().contains(name);
    }

    public Index find(String name) throws IOException {
        if (!this.hasIndex(name)) {
            return null;
        } else {
            return getIndexObjectFromName(name);
        }
    }
}
