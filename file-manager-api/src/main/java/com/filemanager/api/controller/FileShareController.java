package com.filemanager.api.controller;

import com.filemanager.core.service.FileShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/share")
public class FileShareController {
    @Autowired
    private FileShareService fileShareService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createShare(
            @RequestParam Long fileId,
            @RequestParam Long sharedBy,
            @RequestParam(required = false) Long sharedTo,
            @RequestParam String shareType,
            @RequestParam(required = false) String expireTime,
            @RequestParam String permission) {

        Map<String, Object> result = fileShareService.createShare(fileId, sharedBy, sharedTo, shareType, expireTime, permission);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/info/{shareToken}")
    public ResponseEntity<Map<String, Object>> getShareInfo(@PathVariable String shareToken) {
        Map<String, Object> result = fileShareService.getShareInfo(shareToken);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/cancel/{shareId}")
    public ResponseEntity<Map<String, Object>> cancelShare(@PathVariable Long shareId, @RequestParam Long userId) {
        Map<String, Object> result = fileShareService.cancelShare(shareId, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/access/{shareToken}")
    public ResponseEntity<Map<String, Object>> accessShare(@PathVariable String shareToken, @RequestParam Long userId) {
        Map<String, Object> result = fileShareService.accessShare(shareToken, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<Map<String, Object>> getShareList(@PathVariable Long userId) {
        Map<String, Object> result = fileShareService.getShareList(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
