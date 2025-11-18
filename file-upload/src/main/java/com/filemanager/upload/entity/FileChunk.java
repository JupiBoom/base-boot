package com.filemanager.upload.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "file_chunks")
@EqualsAndHashCode(callSuper = false)
public class FileChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_identifier", nullable = false, length = 64)
    private String fileIdentifier;
    @Column(name = "chunk_number", nullable = false)
    private Integer chunkNumber;
    @Column(name = "chunk_size", nullable = false)
    private Long chunkSize;
    @Column(name = "current_chunk_size", nullable = false)
    private Long currentChunkSize;
    @Column(name = "total_size", nullable = false)
    private Long totalSize;
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;
    @Column(name = "file_path", nullable = false, length = 255)
    private String filePath;
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
