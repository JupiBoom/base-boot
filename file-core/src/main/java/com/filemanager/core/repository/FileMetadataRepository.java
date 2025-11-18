package com.filemanager.core.repository;

import com.filemanager.core.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long>, JpaSpecificationExecutor<FileMetadata> {
    List<FileMetadata> findByUploaderId(Long uploaderId);
    FileMetadata findByFileHash(String fileHash);
}
