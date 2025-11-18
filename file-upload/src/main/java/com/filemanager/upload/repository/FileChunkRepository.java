package com.filemanager.upload.repository;

import com.filemanager.upload.entity.FileChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileChunkRepository extends JpaRepository<FileChunk, Long> {
    List<FileChunk> findByFileIdentifier(String fileIdentifier);
    void deleteByFileIdentifier(String fileIdentifier);
}
