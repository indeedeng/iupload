package com.indeed.iupload.core.domain;

import com.indeed.iupload.core.filesystem.FileSystemProxy;
import com.indeed.iupload.core.filesystem.HDFSProxy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author akira
 */
public class Index {
    private final String name;
    private final String pathForIndexedFiles;
    private final String pathForFilesToIndex;
    private final String pathForFailedFiles;

    private FileSystemProxy fileSystemProxy;

    public Index(
            String name,
            String pathForIndexedFiles,
            String pathForFilesToIndex,
            String pathForFailedFiles
    ) throws IOException {
        this.name = name;
        this.pathForFailedFiles = pathForFailedFiles;
        this.pathForFilesToIndex = pathForFilesToIndex;
        this.pathForIndexedFiles = pathForIndexedFiles;
        this.fileSystemProxy = new HDFSProxy();
    }
    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    @JsonIgnore
    private String getPathFor(FileStatus status) {
        switch (status) {
            case Indexed:
                return pathForIndexedFiles;
            case Indexing:
                return pathForFilesToIndex;
            default:
                return pathForFailedFiles;
        }
    }

    public boolean hasFile(FileStatus status, String fileName) throws IOException {
        return fileSystemProxy.listFiles(getPathFor(status)).contains(fileName);
    }

    private FileInfo getFileInfo(FileStatus status, String fileName) throws IOException {
        return new FileInfo(this.getPathFor(status) + File.separator + fileName, status.getName());
    }

    public List<FileInfo> getFileListFor(FileStatus status) throws IOException {
        List<FileInfo> fileList = new ArrayList<FileInfo>();
        for (String fileName : fileSystemProxy.listFiles(this.getPathFor(status))) {
            fileList.add(getFileInfo(status, fileName));
        }
        return fileList;
    }

    public List<FileInfo> getFileList() throws IOException {
        List<FileInfo> fileList = new ArrayList<FileInfo>();
        for (FileStatus status : FileStatus.values()) {
            fileList.addAll(getFileListFor(status));
        }
        return fileList;
    }

    public void createFileToIndex(String name, InputStream inputStream) throws IOException {
        fileSystemProxy.createFile(getPathFor(FileStatus.Indexing), name, inputStream);
    }

    public void deleteFile(FileStatus status, String name) throws IOException {
        fileSystemProxy.remove(getFileInfo(status, name).getPath());
    }

    public FileInfo findFile(FileStatus status, String name) throws IOException {
        if (!hasFile(status, name)) {
            return null;
        }
        return getFileInfo(status, name);
    }
}
