package com.filemanager.core.service.impl;

import com.filemanager.core.entity.FileMetadata;
import com.filemanager.core.repository.FileMetadataRepository;
import com.filemanager.core.service.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileManagerServiceImpl implements FileManagerService {
    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Override
    public Page<FileMetadata> searchFiles(String keyword, Long uploaderId, String fileType, String startDate, String endDate, Pageable pageable) {
        Specification<FileMetadata> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 上传者ID
            if (uploaderId != null) {
                predicates.add(criteriaBuilder.equal(root.get("uploaderId"), uploaderId));
            }

            // 关键词搜索
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("fileName"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("description"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("tags"), "%" + keyword + "%")
                ));
            }

            // 文件类型
            if (fileType != null && !fileType.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("fileType"), fileType));
            }

            // 上传时间范围
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (startDate != null && !startDate.isEmpty()) {
                LocalDateTime start = LocalDateTime.parse(startDate, formatter);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("uploadTime"), start));
            }
            if (endDate != null && !endDate.isEmpty()) {
                LocalDateTime end = LocalDateTime.parse(endDate, formatter);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("uploadTime"), end));
            }

            // 排除已删除文件
            predicates.add(criteriaBuilder.notEqual(root.get("status"), "DELETED"));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return fileMetadataRepository.findAll(specification, pageable);
    }

    @Override
    public Map<String, Object> categorizeFiles(Long uploaderId) {
        Map<String, Object> result = new HashMap<>();

        // 获取所有文件类型
        List<String> fileTypes = fileMetadataRepository.findAll().stream()
                .filter(f -> uploaderId == null || f.getUploaderId().equals(uploaderId))
                .filter(f -> !"DELETED".equals(f.getStatus()))
                .map(FileMetadata::getFileType)
                .distinct()
                .toList();

        Map<String, Long> categoryCount = new HashMap<>();
        Map<String, Long> categorySize = new HashMap<>();

        for (String type : fileTypes) {
            long count = fileMetadataRepository.findAll().stream()
                    .filter(f -> uploaderId == null || f.getUploaderId().equals(uploaderId))
                    .filter(f -> type.equals(f.getFileType()))
                    .filter(f -> !"DELETED".equals(f.getStatus()))
                    .count();

            long size = fileMetadataRepository.findAll().stream()
                    .filter(f -> uploaderId == null || f.getUploaderId().equals(uploaderId))
                    .filter(f -> type.equals(f.getFileType()))
                    .filter(f -> !"DELETED".equals(f.getStatus()))
                    .mapToLong(FileMetadata::getFileSize)
                    .sum();

            categoryCount.put(type, count);
            categorySize.put(type, size);
        }

        result.put("fileTypes", fileTypes);
        result.put("categoryCount", categoryCount);
        result.put("categorySize", categorySize);

        return result;
    }

    @Override
    public Page<FileMetadata> getFileList(Long uploaderId, Pageable pageable) {
        return searchFiles(null, uploaderId, null, null, null, pageable);
    }

    @Override
    public FileMetadata getFileDetail(Long fileId) {
        return fileMetadataRepository.findById(fileId).orElse(null);
    }

    @Override
    public FileMetadata updateFileMetadata(FileMetadata fileMetadata) {
        return fileMetadataRepository.save(fileMetadata);
    }

    @Override
    public Map<String, Object> batchDeleteFiles(List<Long> fileIds) {
        Map<String, Object> result = new HashMap<>();

        List<FileMetadata> files = fileMetadataRepository.findAllById(fileIds);
        long deletedCount = 0;

        for (FileMetadata file : files) {
            if (!"DELETED".equals(file.getStatus())) {
                file.setStatus("DELETED");
                fileMetadataRepository.save(file);
                deletedCount++;
            }
        }

        result.put("success", true);
        result.put("deletedCount", deletedCount);
        result.put("totalCount", fileIds.size());
        result.put("message", "批量删除完成");

        return result;
    }

    @Override
    public Map<String, Object> getStorageUsage(Long uploaderId) {
        Map<String, Object> result = new HashMap<>();

        long totalSize = fileMetadataRepository.findAll().stream()
                .filter(f -> uploaderId == null || f.getUploaderId().equals(uploaderId))
                .filter(f -> !"DELETED".equals(f.getStatus()))
                .mapToLong(FileMetadata::getFileSize)
                .sum();

        long fileCount = fileMetadataRepository.findAll().stream()
                .filter(f -> uploaderId == null || f.getUploaderId().equals(uploaderId))
                .filter(f -> !"DELETED".equals(f.getStatus()))
                .count();

        result.put("success", true);
        result.put("totalSize", totalSize);
        result.put("fileCount", fileCount);

        return result;
    }
}
