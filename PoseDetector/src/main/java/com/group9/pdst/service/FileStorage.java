package com.group9.pdst.service;

import com.group9.pdst.model.FileInfo;

import java.util.List;

public interface FileStorage {
    List<FileInfo> getFileFromLocalStorage(String folderName);
    void deleteAll();
    void createFolder(String folderName);
    void sliceImage(String urlVideo, String folderName);
    void init();
}
