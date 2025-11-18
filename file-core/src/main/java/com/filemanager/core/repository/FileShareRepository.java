package com.filemanager.core.repository;

import com.filemanager.core.entity.FileShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileShareRepository extends JpaRepository<FileShare, Long> {
    List<FileShare> findByShareTokenAndStatus(String shareToken, String status);
    List<FileShare> findBySharedByAndStatus(Long sharedBy, String status);
}
