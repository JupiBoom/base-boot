package com.filemanager.upload.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UploadService {
    /**
     * 初始化分片上传
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @param chunkSize 分片大小
     * @param totalChunks 总分片数
     * @param fileHash 文件Hash
     * @param uploaderId 上传者ID
     * @return 初始化结果
     */
    Map<String, Object> initMultipartUpload(String fileName, Long fileSize, Integer chunkSize, Integer totalChunks, String fileHash, Long uploaderId);

    /**
     * 上传分片
     * @param file 分片文件
     * @param chunkNumber 分片序号
     * @param fileHash 文件Hash
     * @param uploaderId 上传者ID
     * @return 上传结果
     */
    Map<String, Object> uploadChunk(MultipartFile file, Integer chunkNumber, String fileHash, Long uploaderId) throws IOException;

    /**
     * 合并分片
     * @param fileHash 文件Hash
     * @param fileName 文件名
     * @param uploaderId 上传者ID
     * @return 合并结果
     */
    Map<String, Object> mergeChunks(String fileHash, String fileName, Long uploaderId) throws IOException;

    /**
     * 检查分片是否已上传
     * @param fileHash 文件Hash
     * @param chunkNumber 分片序号
     * @param uploaderId 上传者ID
     * @return 检查结果
     */
    Map<String, Object> checkChunk(String fileHash, Integer chunkNumber, Long uploaderId);

    /**
     * 获取上传进度
     * @param fileHash 文件Hash
     * @param uploaderId 上传者ID
     * @return 上传进度
     */
    Map<String, Object> getUploadProgress(String fileHash, Long uploaderId);
}
