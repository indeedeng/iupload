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
