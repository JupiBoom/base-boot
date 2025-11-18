package com.filemanager.core.service;

import com.filemanager.core.entity.FileShare;
import java.util.Map;

public interface FileShareService {
    /**
     * 创建文件分享
     * @param fileId 文件ID
     * @param sharedBy 分享者ID
     * @param sharedTo 被分享者ID
     * @param shareType 分享类型
     * @param expireTime 过期时间
     * @param permission 权限
     * @return 分享结果
     */
    Map<String, Object> createShare(Long fileId, Long sharedBy, Long sharedTo, String shareType, String expireTime, String permission);

    /**
     * 获取分享信息
     * @param shareToken 分享令牌
     * @return 分享信息
     */
    Map<String, Object> getShareInfo(String shareToken);

    /**
     * 取消分享
     * @param shareId 分享ID
     * @param userId 用户ID
     * @return 取消结果
     */
    Map<String, Object> cancelShare(Long shareId, Long userId);

    /**
     * 访问分享文件
     * @param shareToken 分享令牌
     * @param userId 用户ID
     * @return 访问结果
     */
    Map<String, Object> accessShare(String shareToken, Long userId);

    /**
     * 获取用户分享列表
     * @param userId 用户ID
     * @return 分享列表
     */
    Map<String, Object> getShareList(Long userId);
}
