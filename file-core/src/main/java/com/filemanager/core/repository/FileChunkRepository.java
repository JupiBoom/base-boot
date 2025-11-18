package com.filemanager.core.repository;

import com.filemanager.core.entity.FileChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileChunkRepository extends JpaRepository<FileChunk, Long> {
    List<FileChunk> findByFileHashAndUploaderId(String fileHash, Long uploaderId);
    FileChunk findByFileHashAndChunkNumberAndUploaderId(String fileHash, Integer chunkNumber, Long uploaderId);
    long countByFileHashAndUploaderId(String fileHash, Long uploaderId);
}
