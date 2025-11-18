package com.filemanager.api.controller;

import com.filemanager.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initMultipartUpload(
            @RequestParam String fileName,
            @RequestParam Long fileSize,
            @RequestParam Integer chunkSize,
            @RequestParam Integer totalChunks,
            @RequestParam String fileHash,
            @RequestParam Long uploaderId) {

        Map<String, Object> result = uploadService.initMultipartUpload(fileName, fileSize, chunkSize, totalChunks, fileHash, uploaderId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/chunk")
    public ResponseEntity<Map<String, Object>> uploadChunk(
            @RequestParam("file") MultipartFile file,
            @RequestParam Integer chunkNumber,
            @RequestParam String fileHash,
            @RequestParam Long uploaderId) throws IOException {

        Map<String, Object> result = uploadService.uploadChunk(file, chunkNumber, fileHash, uploaderId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/merge")
    public ResponseEntity<Map<String, Object>> mergeChunks(
            @RequestParam String fileHash,
            @RequestParam String fileName,
            @RequestParam Long uploaderId) throws IOException {

        Map<String, Object> result = uploadService.mergeChunks(fileHash, fileName, uploaderId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkChunk(
            @RequestParam String fileHash,
            @RequestParam Integer chunkNumber,
            @RequestParam Long uploaderId) {

        Map<String, Object> result = uploadService.checkChunk(fileHash, chunkNumber, uploaderId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/progress")
    public ResponseEntity<Map<String, Object>> getUploadProgress(
            @RequestParam String fileHash,
            @RequestParam Long uploaderId) {

        Map<String, Object> result = uploadService.getUploadProgress(fileHash, uploaderId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
