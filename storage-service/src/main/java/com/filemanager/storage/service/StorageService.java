package com.filemanager.storage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface StorageService {
    /**
     * 上传文件
     * @param file 文件
     * @param fileName 文件名
     * @param storageType 存储类型
     * @return 上传结果
     */
    Map<String, Object> uploadFile(MultipartFile file, String fileName, String storageType) throws IOException;

    /**
     * 下载文件
     * @param fileId 文件ID
     * @return 文件输入流
     */
    InputStream downloadFile(Long fileId) throws IOException;

    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 删除结果
     */
    Map<String, Object> deleteFile(Long fileId);

    /**
     * 预览文件
     * @param fileId 文件ID
     * @return 预览URL
     */
    Map<String, Object> previewFile(Long fileId);

    /**
     * 获取文件信息
     * @param fileId 文件ID
     * @return 文件信息
     */
    Map<String, Object> getFileInfo(Long fileId);
}
