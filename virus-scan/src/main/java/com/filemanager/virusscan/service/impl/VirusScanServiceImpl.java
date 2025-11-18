package com.filemanager.virusscan.service.impl;

import com.filemanager.core.entity.FileMetadata;
import com.filemanager.core.repository.FileMetadataRepository;
import com.filemanager.virusscan.service.VirusScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class VirusScanServiceImpl implements VirusScanService {
    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Value("${clamav.host}")
    private String clamavHost;

    @Value("${clamav.port}")
    private Integer clamavPort;

    @Value("${file.quarantine.path}")
    private String quarantinePath;

    private static final int CLAMAV_TIMEOUT = 30000;
    private static final int BUFFER_SIZE = 1024;

    @Override
    public Map<String, Object> scanFile(File file, Long fileId) throws IOException {
        Map<String, Object> result = new HashMap<>();

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(clamavHost, clamavPort), CLAMAV_TIMEOUT);

            try (OutputStream os = socket.getOutputStream();
                 InputStream is = socket.getInputStream();
                 BufferedOutputStream bos = new BufferedOutputStream(os);
                 FileInputStream fis = new FileInputStream(file)) {

                // 发送SCAN命令
                String command = "SCAN " + file.getAbsolutePath() + "\0";
                bos.write(command.getBytes());
                bos.flush();

                // 读取响应
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = is.read(buffer);
                if (bytesRead > 0) {
                    String response = new String(buffer, 0, bytesRead);
                    log.info("ClamAV扫描响应: {}", response);

                    FileMetadata fileMetadata = fileMetadataRepository.findById(fileId).orElse(null);
                    if (fileMetadata != null) {
                        if (response.contains("FOUND")) {
                            fileMetadata.setStatus("INFECTED");
                            result.put("infected", true);
                            result.put("message", "文件包含病毒");
                            result.put("virusName", response.substring(response.indexOf(":") + 2, response.indexOf("FOUND") - 1));
                        } else if (response.contains("OK")) {
                            fileMetadata.setStatus("SCANNED");
                            result.put("infected", false);
                            result.put("message", "文件安全");
                        } else {
                            fileMetadata.setStatus("SCAN_FAILED");
                            result.put("infected", false);
                            result.put("message", "扫描失败: " + response);
                        }
                        fileMetadataRepository.save(fileMetadata);
                    }
                }
            }
        } catch (Exception e) {
            log.error("ClamAV扫描失败: {}", e.getMessage());
            result.put("infected", false);
            result.put("message", "扫描服务不可用: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> getScanResult(Long fileId) {
        Map<String, Object> result = new HashMap<>();

        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId).orElse(null);
        if (fileMetadata == null) {
            result.put("success", false);
            result.put("message", "文件不存在");
            return result;
        }

        result.put("success", true);
        result.put("fileId", fileId);
        result.put("status", fileMetadata.getStatus());
        result.put("infected", "INFECTED".equals(fileMetadata.getStatus()));

        return result;
    }

    @Override
    public Map<String, Object> quarantineFile(Long fileId) {
        Map<String, Object> result = new HashMap<>();

        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId).orElse(null);
        if (fileMetadata == null) {
            result.put("success", false);
            result.put("message", "文件不存在");
            return result;
        }

        if (!"INFECTED".equals(fileMetadata.getStatus())) {
            result.put("success", false);
            result.put("message", "只有感染的文件才能被隔离");
            return result;
        }

        try {
            File sourceFile = new File(fileMetadata.getFilePath());
            File quarantineDir = new File(quarantinePath);
            if (!quarantineDir.exists()) {
                quarantineDir.mkdirs();
            }

            File destFile = new File(quarantineDir, sourceFile.getName());
            boolean moved = sourceFile.renameTo(destFile);
            if (moved) {
                fileMetadata.setStatus("QUARANTINED");
                fileMetadata.setFilePath(destFile.getAbsolutePath());
                fileMetadataRepository.save(fileMetadata);

                result.put("success", true);
                result.put("message", "文件已隔离");
                result.put("quarantinePath", destFile.getAbsolutePath());
            } else {
                result.put("success", false);
                result.put("message", "文件隔离失败");
            }
        } catch (Exception e) {
            log.error("文件隔离失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "文件隔离失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> deleteInfectedFile(Long fileId) {
        Map<String, Object> result = new HashMap<>();

        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId).orElse(null);
        if (fileMetadata == null) {
            result.put("success", false);
            result.put("message", "文件不存在");
            return result;
        }

        if (!"INFECTED".equals(fileMetadata.getStatus()) && !"QUARANTINED".equals(fileMetadata.getStatus())) {
            result.put("success", false);
            result.put("message", "只有感染或已隔离的文件才能被删除");
            return result;
        }

        try {
            File file = new File(fileMetadata.getFilePath());
            if (file.exists() && file.delete()) {
                fileMetadata.setStatus("DELETED");
                fileMetadataRepository.save(fileMetadata);

                result.put("success", true);
                result.put("message", "感染文件已删除");
            } else {
                result.put("success", false);
                result.put("message", "文件删除失败");
            }
        } catch (Exception e) {
            log.error("删除感染文件失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "删除感染文件失败: " + e.getMessage());
        }

        return result;
    }
}
