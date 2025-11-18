package com.filemanager.storage.service.impl;

import com.filemanager.core.entity.FileMetadata;
import com.filemanager.core.repository.FileMetadataRepository;
import com.filemanager.storage.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class LocalStorageServiceImpl implements StorageService {
    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.preview.base-url}")
    private String previewBaseUrl;

    @Override
    public Map<String, Object> uploadFile(MultipartFile file, String fileName, String storageType) throws IOException {
        Map<String, Object> result = new HashMap<>();

        // 创建上传目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 生成唯一文件名
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
        File destFile = new File(uploadDir, uniqueFileName);

        // 保存文件
        file.transferTo(destFile);

        result.put("success", true);
        result.put("filePath", destFile.getAbsolutePath());
        result.put("uniqueFileName", uniqueFileName);
        result.put("storageType", "LOCAL");
        result.put("fileSize", file.getSize());

        return result;
    }

    @Override
    public InputStream downloadFile(Long fileId) throws IOException {
        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId).orElse(null);
        if (fileMetadata == null || "DELETED".equals(fileMetadata.getStatus())) {
            throw new FileNotFoundException("文件不存在或已删除");
        }

        File file = new File(fileMetadata.getFilePath());
        return new FileInputStream(file);
    }

    @Override
    public Map<String, Object> deleteFile(Long fileId) {
        Map<String, Object> result = new HashMap<>();

        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId).orElse(null);
        if (fileMetadata == null) {
            result.put("success", false);
            result.put("message", "文件不存在");
            return result;
        }

        try {
            File file = new File(fileMetadata.getFilePath());
            if (file.exists() && file.delete()) {
                fileMetadata.setStatus("DELETED");
                fileMetadataRepository.save(fileMetadata);

                result.put("success", true);
                result.put("message", "文件删除成功");
            } else {
                result.put("success", false);
                result.put("message", "文件删除失败");
            }
        } catch (Exception e) {
            log.error("删除文件失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "文件删除失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> previewFile(Long fileId) {
        Map<String, Object> result = new HashMap<>();

        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId).orElse(null);
        if (fileMetadata == null || "DELETED".equals(fileMetadata.getStatus())) {
            result.put("success", false);
            result.put("message", "文件不存在或已删除");
            return result;
        }

        // 生成预览URL
        String previewUrl = previewBaseUrl + "/api/storage/preview/" + fileId;
        result.put("success", true);
        result.put("previewUrl", previewUrl);
        result.put("contentType", fileMetadata.getContentType());

        return result;
    }

    @Override
    public Map<String, Object> getFileInfo(Long fileId) {
        Map<String, Object> result = new HashMap<>();

        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId).orElse(null);
        if (fileMetadata == null) {
            result.put("success", false);
            result.put("message", "文件不存在");
            return result;
        }

        result.put("success", true);
        result.put("fileId", fileMetadata.getId());
        result.put("fileName", fileMetadata.getFileName());
        result.put("fileSize", fileMetadata.getFileSize());
        result.put("fileType", fileMetadata.getFileType());
        result.put("uploadTime", fileMetadata.getUploadTime());
        result.put("status", fileMetadata.getStatus());
        result.put("storageType", fileMetadata.getStorageType());
        result.put("contentType", fileMetadata.getContentType());

        return result;
    }
}
