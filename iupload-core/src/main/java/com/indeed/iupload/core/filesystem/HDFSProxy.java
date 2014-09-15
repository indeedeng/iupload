package com.indeed.iupload.core.filesystem;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author akira
 */
public class HDFSProxy implements FileSystemProxy {

    private final Configuration configuration;

    public HDFSProxy() {
        configuration = new Configuration();
    }

    private FileSystem getFileSystem(String path) throws IOException {
        return new Path(path).getFileSystem(configuration);
    }

    @Override
    public List<String> listFiles(String path) throws IOException {
        final Path pathObj = new Path(path);
        if(!getFileSystem(path).exists(pathObj)) {
            return Lists.newArrayList();
        }
        List<String> results = new ArrayList<String>();
        for (FileStatus status : getFileSystem(path).listStatus(pathObj)) {
            if (!status.isDirectory()) {
                results.add(status.getPath().getName());
            }
        }
        return results;
    }

    @Override
    public List<String> listDirs(String path) throws IOException {
        final Path pathObj = new Path(path);
        if(!getFileSystem(path).exists(pathObj)) {
            return Lists.newArrayList();
        }
        List<String> results = new ArrayList<String>();
        for (FileStatus status : getFileSystem(path).listStatus(pathObj)) {
            if (status.isDirectory()) {
                results.add(status.getPath().getName());
            }
        }
        return results;
    }

    @Override
    public void createDir(String path, String name) throws IOException {
        getFileSystem(path).mkdirs(new Path(path, name));
    }

    @Override
    public void createFile(String path, String name, InputStream inputStream) throws IOException {
        final Path pathObj = new Path(path, name);
        if(!getFileSystem(path).exists(pathObj)) {
            mkdirs(path);
        }
        OutputStream outputStream = getFileSystem(path).create(pathObj);
        ByteStreams.copy(inputStream, outputStream);
        outputStream.close();
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        return getFileSystem(path).open(new Path(path));
    }

    @Override
    public void mkdirs(String rootPath) throws IOException {
        Path p = new Path(rootPath);
        if (!getFileSystem(rootPath).exists(p)) {
            getFileSystem(rootPath).mkdirs(new Path(rootPath));
        }
    }

    @Override
    public String getName(String path) throws IOException {
        return new Path(path).getName();
    }

    @Override
    public long getSize(String path) throws IOException {
        return getFileSystem(path).getFileStatus(new Path(path)).getLen();
    }

    @Override
    public Date getLastModifiedAt(String path) throws IOException {
        return new Date(getFileSystem(path).getFileStatus(new Path(path)).getModificationTime());
    }

    @Override
    public void remove(String path) throws IOException {
        getFileSystem(path).delete(new Path(path), true);
    }
}
