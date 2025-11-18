package com.filemanager.core.service;

import com.filemanager.core.entity.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface FileManagerService {
    /**
     * 搜索文件
     * @param keyword 关键词
     * @param uploaderId 上传者ID
     * @param fileType 文件类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageable 分页参数
     * @return 搜索结果
     */
    Page<FileMetadata> searchFiles(String keyword, Long uploaderId, String fileType, String startDate, String endDate, Pageable pageable);

    /**
     * 分类统计文件
     * @param uploaderId 上传者ID
     * @return 分类统计结果
     */
    Map<String, Object> categorizeFiles(Long uploaderId);

    /**
     * 获取文件列表
     * @param uploaderId 上传者ID
     * @param pageable 分页参数
     * @return 文件列表
     */
    Page<FileMetadata> getFileList(Long uploaderId, Pageable pageable);

    /**
     * 获取文件详情
     * @param fileId 文件ID
     * @return 文件详情
     */
    FileMetadata getFileDetail(Long fileId);

    /**
     * 更新文件元数据
     * @param fileMetadata 文件元数据
     * @return 更新后的文件元数据
     */
    FileMetadata updateFileMetadata(FileMetadata fileMetadata);

    /**
     * 批量删除文件
     * @param fileIds 文件ID列表
     * @return 删除结果
     */
    Map<String, Object> batchDeleteFiles(List<Long> fileIds);

    /**
     * 获取存储空间使用情况
     * @param uploaderId 上传者ID
     * @return 存储空间使用情况
     */
    Map<String, Object> getStorageUsage(Long uploaderId);
}
