package com.filemanager.upload.service.impl;

import com.filemanager.core.entity.FileChunk;
import com.filemanager.core.entity.FileMetadata;
import com.filemanager.core.repository.FileChunkRepository;
import com.filemanager.core.repository.FileMetadataRepository;
import com.filemanager.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private FileChunkRepository fileChunkRepository;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${file.upload.temp.path}")
    private String tempUploadPath;

    @Value("${file.upload.path}")
    private String finalUploadPath;

    private static final String UPLOAD_PROGRESS_KEY = "upload:progress:";
    private static final String UPLOAD_CHUNKS_KEY = "upload:chunks:";

    @Override
    public Map<String, Object> initMultipartUpload(String fileName, Long fileSize, Integer chunkSize, Integer totalChunks, String fileHash, Long uploaderId) {
        Map<String, Object> result = new HashMap<>();

        // 检查文件是否已存在
        FileMetadata existingFile = fileMetadataRepository.findByFileHash(fileHash);
        if (existingFile != null) {
            result.put("success", true);
            result.put("fileId", existingFile.getId());
            result.put("message", "文件已存在");
            return result;
        }

        // 创建临时目录
        Path tempDirPath = Paths.get(tempUploadPath, fileHash);
        if (!Files.exists(tempDirPath)) {
            try {
                Files.createDirectories(tempDirPath);
            } catch (IOException e) {
                log.error("创建临时目录失败: {}", e.getMessage());
                result.put("success", false);
                result.put("message", "创建临时目录失败");
                return result;
            }
        }

        // 保存上传进度到Redis
        Map<String, Object> progressInfo = new HashMap<>();
        progressInfo.put("totalChunks", totalChunks);
        progressInfo.put("uploadedChunks", 0);
        progressInfo.put("fileSize", fileSize);
        progressInfo.put("fileName", fileName);
        progressInfo.put("fileHash", fileHash);
        progressInfo.put("uploaderId", uploaderId);

        String progressKey = UPLOAD_PROGRESS_KEY + fileHash + ":" + uploaderId;
        redisTemplate.opsForHash().putAll(progressKey, progressInfo);
        redisTemplate.expire(progressKey, 24, TimeUnit.HOURS);

        result.put("success", true);
        result.put("message", "初始化成功");
        result.put("fileHash", fileHash);
        result.put("totalChunks", totalChunks);
        return result;
    }

    @Override
    public Map<String, Object> uploadChunk(MultipartFile file, Integer chunkNumber, String fileHash, Long uploaderId) throws IOException {
        Map<String, Object> result = new HashMap<>();

        // 检查文件是否已合并完成
        FileMetadata existingFile = fileMetadataRepository.findByFileHash(fileHash);
        if (existingFile != null) {
            result.put("success", true);
            result.put("fileId", existingFile.getId());
            result.put("message", "文件已存在");
            return result;
        }

        // 保存分片到临时目录
        Path tempDirPath = Paths.get(tempUploadPath, fileHash);
        Path chunkPath = tempDirPath.resolve(chunkNumber.toString());

        Files.write(chunkPath, file.getBytes());

        // 保存分片信息到数据库
        FileChunk fileChunk = new FileChunk();
        fileChunk.setChunkNumber(chunkNumber);
        fileChunk.setChunkSize(file.getSize());
        fileChunk.setFileName(file.getOriginalFilename());
        fileChunk.setFileHash(fileHash);
        fileChunk.setUploaderId(uploaderId);
        fileChunk.setStatus("UPLOADED");

        fileChunkRepository.save(fileChunk);

        // 更新上传进度
        String progressKey = UPLOAD_PROGRESS_KEY + fileHash + ":" + uploaderId;
        redisTemplate.opsForHash().increment(progressKey, "uploadedChunks", 1);

        long uploadedChunks = (long) redisTemplate.opsForHash().get(progressKey, "uploadedChunks");
        int totalChunks = (int) redisTemplate.opsForHash().get(progressKey, "totalChunks");

        result.put("success", true);
        result.put("message", "分片上传成功");
        result.put("chunkNumber", chunkNumber);
        result.put("uploadedChunks", uploadedChunks);
        result.put("totalChunks", totalChunks);
        result.put("progress", (uploadedChunks * 100.0) / totalChunks);

        return result;
    }

    @Override
    public Map<String, Object> mergeChunks(String fileHash, String fileName, Long uploaderId) throws IOException {
        Map<String, Object> result = new HashMap<>();

        // 检查文件是否已存在
        FileMetadata existingFile = fileMetadataRepository.findByFileHash(fileHash);
        if (existingFile != null) {
            result.put("success", true);
            result.put("fileId", existingFile.getId());
            result.put("message", "文件已存在");
            return result;
        }

        // 获取所有分片
        List<FileChunk> chunks = fileChunkRepository.findByFileHashAndUploaderId(fileHash, uploaderId);
        if (chunks.isEmpty()) {
            result.put("success", false);
            result.put("message", "分片不存在");
            return result;
        }

        // 按分片序号排序
        chunks.sort(Comparator.comparingInt(FileChunk::getChunkNumber));

        // 创建最终文件
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String finalFileName = UUID.randomUUID().toString() + "." + fileExtension;
        Path finalFilePath = Paths.get(finalUploadPath, finalFileName);

        try (FileOutputStream fos = new FileOutputStream(finalFilePath.toFile());
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            for (FileChunk chunk : chunks) {
                Path chunkPath = Paths.get(tempUploadPath, fileHash, chunk.getChunkNumber().toString());
                Files.copy(chunkPath, bos);
                // 删除临时分片文件
                Files.deleteIfExists(chunkPath);
            }
        }

        // 删除临时目录
        Path tempDirPath = Paths.get(tempUploadPath, fileHash);
        Files.deleteIfExists(tempDirPath);

        // 保存文件元数据
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setFileName(fileName);
        fileMetadata.setFileSize(chunks.stream().mapToLong(FileChunk::getChunkSize).sum());
        fileMetadata.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
        fileMetadata.setFilePath(finalFilePath.toString());
        fileMetadata.setFileHash(fileHash);
        fileMetadata.setUploaderId(uploaderId);
        fileMetadata.setStatus("UPLOADED");
        fileMetadata.setStorageType("LOCAL");
        fileMetadata.setContentType("application/octet-stream");

        fileMetadataRepository.save(fileMetadata);

        // 更新上传状态
        String progressKey = UPLOAD_PROGRESS_KEY + fileHash + ":" + uploaderId;
        redisTemplate.delete(progressKey);

        // 删除分片信息
        fileChunkRepository.deleteAll(chunks);

        result.put("success", true);
        result.put("message", "文件合并成功");
        result.put("fileId", fileMetadata.getId());
        result.put("fileName", fileName);
        result.put("fileSize", fileMetadata.getFileSize());
        result.put("filePath", fileMetadata.getFilePath());

        return result;
    }

    @Override
    public Map<String, Object> checkChunk(String fileHash, Integer chunkNumber, Long uploaderId) {
        Map<String, Object> result = new HashMap<>();

        // 检查分片是否已上传
        FileChunk chunk = fileChunkRepository.findByFileHashAndChunkNumberAndUploaderId(fileHash, chunkNumber, uploaderId);
        if (chunk != null) {
            result.put("success", true);
            result.put("exists", true);
            result.put("message", "分片已存在");
        } else {
            result.put("success", true);
            result.put("exists", false);
            result.put("message", "分片不存在");
        }

        return result;
    }

    @Override
    public Map<String, Object> getUploadProgress(String fileHash, Long uploaderId) {
        Map<String, Object> result = new HashMap<>();
        String progressKey = UPLOADED" + fileHash + ":" + uploaderId;

        if (redisTemplate.hasKey(progressKey)) {
            Map<Object, Object> progressInfo = redisTemplate.opsForHash().entries(progressKey);
            long uploadedChunks = (long) progressInfo.get("uploadedChunks");
            int totalChunks = (int) progressInfo.get("totalChunks");

            result.put("success", true);
            result.put("progress", (uploadedChunks * 100.0) / totalChunks);
            result.put("uploadedChunks", uploadedChunks);
            result.put("totalChunks", totalChunks);
            result.put("fileName", progressInfo.get("fileName"));
        } else {
            // 检查文件是否已合并完成
            FileMetadata existingFile = fileMetadataRepository.findByFileHash(fileHash);
            if (existingFile != null) {
                result.put("success", true);
                result.put("progress", 100.0);
                result.put("message", "文件已上传完成");
            } else {
                result.put("success", false);
                result.put("message", "上传记录不存在");
            }
        }

        return result;
    }
}
