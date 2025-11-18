package com.filemanager.core.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "file_chunk")
@EntityListeners(AuditingEntityListener.class)
public class FileChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chunk_number", nullable = false)
    private Integer chunkNumber;

    @Column(name = "chunk_size", nullable = false)
    private Long chunkSize;

    @Column(name = "total_chunks", nullable = false)
    private Integer totalChunks;

    @Column(name = "total_size", nullable = false)
    private Long totalSize;

    @Column(name = "file_hash", nullable = false)
    private String fileHash;

    @Column(name = "chunk_hash")
    private String chunkHash;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "uploader_id", nullable = false)
    private Long uploaderId;

    @Column(name = "status")
    private String status; // UPLOADED, MERGED

    @Column(name = "created_time")
    @CreatedDate
    private LocalDateTime createdTime;
}
