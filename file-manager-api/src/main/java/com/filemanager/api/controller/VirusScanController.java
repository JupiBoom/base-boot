package com.filemanager.api.controller;

import com.filemanager.core.entity.FileMetadata;
import com.filemanager.core.repository.FileMetadataRepository;
import com.filemanager.virusscan.service.VirusScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/virus-scan")
public class VirusScanController {
    @Autowired
    private VirusScanService virusScanService;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @PostMapping("/scan/{fileId}")
    public ResponseEntity<Map<String, Object>> scanFile(@PathVariable Long fileId) throws IOException {
        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId).orElse(null);
        if (fileMetadata == null) {
            return new ResponseEntity<>(Map.of("success", false, "message", "文件不存在"), HttpStatus.NOT_FOUND);
        }

        File file = new File(fileMetadata.getFilePath());
        Map<String, Object> result = virusScanService.scanFile(file, fileId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/result/{fileId}")
    public ResponseEntity<Map<String, Object>> getScanResult(@PathVariable Long fileId) {
        Map<String, Object> result = virusScanService.getScanResult(fileId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/quarantine/{fileId}")
    public ResponseEntity<Map<String, Object>> quarantineFile(@PathVariable Long fileId) {
        Map<String, Object> result = virusScanService.quarantineFile(fileId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteInfectedFile(@PathVariable Long fileId) {
        Map<String, Object> result = virusScanService.deleteInfectedFile(fileId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
