package com.filemanager.virusscan.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface VirusScanService {
    /**
     * 扫描文件是否包含病毒
     * @param file 文件对象
     * @param fileId 文件ID
     * @return 扫描结果
     */
    Map<String, Object> scanFile(File file, Long fileId) throws IOException;

    /**
     * 获取扫描结果
     * @param fileId 文件ID
     * @return 扫描结果
     */
    Map<String, Object> getScanResult(Long fileId);

    /**
     * 隔离感染文件
     * @param fileId 文件ID
     * @return 隔离结果
     */
    Map<String, Object> quarantineFile(Long fileId);

    /**
     * 删除感染文件
     * @param fileId 文件ID
     * @return 删除结果
     */
    Map<String, Object> deleteInfectedFile(Long fileId);
}
