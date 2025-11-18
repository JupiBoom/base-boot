package com.filemanager.api.controller;

import com.filemanager.core.entity.FileMetadata;
import com.filemanager.core.service.FileManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileManagerController {
    @Autowired
    private FileManagerService fileManagerService;

    @GetMapping("/search")
    public ResponseEntity<Page<FileMetadata>> searchFiles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long uploaderId,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "uploadTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<FileMetadata> result = fileManagerService.searchFiles(keyword, uploaderId, fileType, startDate, endDate, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/categorize/{uploaderId}")
    public ResponseEntity<Map<String, Object>> categorizeFiles(@PathVariable Long uploaderId) {
        Map<String, Object> result = fileManagerService.categorizeFiles(uploaderId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/list/{uploaderId}")
    public ResponseEntity<Page<FileMetadata>> getFileList(
            @PathVariable Long uploaderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "uploadTime"));
        Page<FileMetadata> result = fileManagerService.getFileList(uploaderId, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/detail/{fileId}")
    public ResponseEntity<FileMetadata> getFileDetail(@PathVariable Long fileId) {
        FileMetadata fileMetadata = fileManagerService.getFileDetail(fileId);
        if (fileMetadata == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(fileMetadata, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<FileMetadata> updateFileMetadata(@RequestBody FileMetadata fileMetadata) {
        FileMetadata updatedFile = fileManagerService.updateFileMetadata(fileMetadata);
        return new ResponseEntity<>(updatedFile, HttpStatus.OK);
    }

    @DeleteMapping("/batch-delete")
    public ResponseEntity<Map<String, Object>> batchDeleteFiles(@RequestBody List<Long> fileIds) {
        Map<String, Object> result = fileManagerService.batchDeleteFiles(fileIds);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/storage-usage/{uploaderId}")
    public ResponseEntity<Map<String, Object>> getStorageUsage(@PathVariable Long uploaderId) {
        Map<String, Object> result = fileManagerService.getStorageUsage(uploaderId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
