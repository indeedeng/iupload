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
