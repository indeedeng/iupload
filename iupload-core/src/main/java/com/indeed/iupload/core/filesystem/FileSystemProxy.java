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
