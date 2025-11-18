package com.filemanager.api.controller;

import com.filemanager.storage.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) throws IOException {
        InputStream inputStream = storageService.downloadFile(fileId);
        byte[] fileContent = inputStream.readAllBytes();
        inputStream.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "file_" + fileId);
        headers.setContentLength(fileContent.length);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    @GetMapping("/preview/{fileId}")
    public ResponseEntity<byte[]> previewFile(@PathVariable Long fileId) throws IOException {
        InputStream inputStream = storageService.downloadFile(fileId);
        byte[] fileContent = inputStream.readAllBytes();
        inputStream.close();

        Map<String, Object> fileInfo = storageService.getFileInfo(fileId);
        String contentType = (String) fileInfo.get("contentType");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"));
        headers.setContentDispositionFormData("inline", "file_" + fileId);
        headers.setContentLength(fileContent.length);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    @GetMapping("/info/{fileId}")
    public ResponseEntity<Map<String, Object>> getFileInfo(@PathVariable Long fileId) {
        Map<String, Object> result = storageService.getFileInfo(fileId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable Long fileId) {
        Map<String, Object> result = storageService.deleteFile(fileId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
