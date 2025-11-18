package com.filemanager.core.service.impl;

import com.filemanager.core.entity.FileMetadata;
import com.filemanager.core.entity.FileShare;
import com.filemanager.core.repository.FileMetadataRepository;
import com.filemanager.core.repository.FileShareRepository;
import com.filemanager.core.service.FileShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FileShareServiceImpl implements FileShareService {
    @Autowired
    private FileShareRepository fileShareRepository;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Override
    public Map<String, Object> createShare(Long fileId, Long sharedBy, Long sharedTo, String shareType, String expireTime, String permission) {
        Map<String, Object> result = new HashMap<>();

        // 检查文件是否存在
        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId).orElse(null);
        if (fileMetadata == null || "DELETED".equals(fileMetadata.getStatus())) {
            result.put("success", false);
            result.put("message", "文件不存在或已删除");
            return result;
        }

        // 创建分享
        FileShare fileShare = new FileShare();
        fileShare.setFileId(fileId);
        fileShare.setSharedBy(sharedBy);
        fileShare.setSharedTo(sharedTo);
        fileShare.setShareType(shareType);
        fileShare.setPermission(permission);
        fileShare.setStatus("ACTIVE");

        // 生成分享令牌
        String shareToken = UUID.randomUUID().toString().replace("-", "");
        fileShare.setShareToken(shareToken);

        // 设置过期时间
        if (expireTime != null && !expireTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime expire = LocalDateTime.parse(expireTime, formatter);
            fileShare.setExpireTime(expire);
        }

        fileShareRepository.save(fileShare);

        result.put("success", true);
        result.put("message", "分享创建成功");
        result.put("shareToken", shareToken);
        result.put("shareId", fileShare.getId());

        return result;
    }

    @Override
    public Map<String, Object> getShareInfo(String shareToken) {
        Map<String, Object> result = new HashMap<>();

        List<FileShare> fileShares = fileShareRepository.findByShareTokenAndStatus(shareToken, "ACTIVE");
        if (fileShares.isEmpty()) {
            result.put("success", false);
            result.put("message", "分享不存在或已过期");
            return result;
        }

        FileShare fileShare = fileShares.get(0);

        // 检查是否过期
        if (fileShare.getExpireTime() != null && LocalDateTime.now().isAfter(fileShare.getExpireTime())) {
            fileShare.setStatus("EXPIRED");
            fileShareRepository.save(fileShare);
            result.put("success", false);
            result.put("message", "分享已过期");
            return result;
        }

        // 获取文件信息
        FileMetadata fileMetadata = fileMetadataRepository.findById(fileShare.getFileId()).orElse(null);
        if (fileMetadata == null) {
            result.put("success", false);
            result.put("message", "文件不存在");
            return result;
        }

        Map<String, Object> shareInfo = new HashMap<>();
        shareInfo.put("shareId", fileShare.getId());
        shareInfo.put("fileId", fileShare.getFileId());
        shareInfo.put("shareType", fileShare.getShareType());
        shareInfo.put("permission", fileShare.getPermission());
        shareInfo.put("expireTime", fileShare.getExpireTime());
        shareInfo.put("createdTime", fileShare.getCreatedTime());
        shareInfo.put("file", fileMetadata);

        result.put("success", true);
        result.put("shareInfo", shareInfo);

        return result;
    }

    @Override
    public Map<String, Object> cancelShare(Long shareId, Long userId) {
        Map<String, Object> result = new HashMap<>();

        FileShare fileShare = fileShareRepository.findById(shareId).orElse(null);
        if (fileShare == null) {
            result.put("success", false);
            result.put("message", "分享不存在");
            return result;
        }

        // 检查是否有权限取消分享
        if (!fileShare.getSharedBy().equals(userId)) {
            result.put("success", false);
            result.put("message", "没有权限取消分享");
            return result;
        }

        fileShare.setStatus("REVOKED");
        fileShareRepository.save(fileShare);

        result.put("success", true);
        result.put("message", "分享已取消");

        return result;
    }

    @Override
    public Map<String, Object> accessShare(String shareToken, Long userId) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> shareInfoResult = getShareInfo(shareToken);
        if (!(Boolean) shareInfoResult.get("success")) {
            return shareInfoResult;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> shareInfo = (Map<String, Object>) shareInfoResult.get("shareInfo");
        FileShare fileShare = fileShareRepository.findById((Long) shareInfo.get("shareId")).orElse(null);
        if (fileShare == null) {
            result.put("success", false);
            result.put("message", "分享不存在");
            return result;
        }

        // 检查权限
        if ("PRIVATE".equals(fileShare.getShareType()) && !fileShare.getSharedTo().equals(userId)) {
            result.put("success", false);
            result.put("message", "没有权限访问该分享");
            return result;
        }

        result.put("success", true);
        result.put("message", "访问成功");
        result.put("shareInfo", shareInfo);

        return result;
    }

    @Override
    public Map<String, Object> getShareList(Long userId) {
        Map<String, Object> result = new HashMap<>();

        List<FileShare> shareList = fileShareRepository.findBySharedByAndStatus(userId, "ACTIVE");
        // 过滤过期分享
        shareList.forEach(share -> {
            if (share.getExpireTime() != null && LocalDateTime.now().isAfter(share.getExpireTime())) {
                share.setStatus("EXPIRED");
                fileShareRepository.save(share);
            }
        });

        // 获取有效分享
        List<FileShare> activeShares = fileShareRepository.findBySharedByAndStatus(userId, "ACTIVE");

        result.put("success", true);
        result.put("shareList", activeShares);
        result.put("total", activeShares.size());

        return result;
    }
}
