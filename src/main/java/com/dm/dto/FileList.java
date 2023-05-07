package com.dm.dto;

import java.util.ArrayList;
import java.util.List;

public class FileList {

    private final String path;
    private final List<FileInfo> fileInfo;
    private final List<DirectoryInfo> directoryInfo;

    public FileList(String path) {
        this.path = path;
        this.fileInfo = new ArrayList<>();
        this.directoryInfo = new ArrayList<>();
    }

    public FileList(String path, List<FileInfo> fileInfo, List<DirectoryInfo> directoryInfo) {
        this.path = path;
        this.fileInfo = fileInfo;
        this.directoryInfo = directoryInfo;
    }

    public String getPath() {
        return path;
    }

    public List<FileInfo> getFileInfo() {
        return fileInfo;
    }

    public List<DirectoryInfo> getDirectoryInfo() {
        return directoryInfo;
    }

    public void add(FileInfo fileInfo) {
        this.fileInfo.add(fileInfo);
    }

    public void add(DirectoryInfo directoryInfo) {
        this.directoryInfo.add(directoryInfo);
    }

}
