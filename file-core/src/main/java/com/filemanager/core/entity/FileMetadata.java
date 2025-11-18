package com.filemanager.core.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "file_metadata")
@EntityListeners(AuditingEntityListener.class)
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_hash")
    private String fileHash;

    @Column(name = "uploader_id", nullable = false)
    private Long uploaderId;

    @Column(name = "upload_time")
    @CreatedDate
    private LocalDateTime uploadTime;

    @Column(name = "status")
    private String status; // PENDING, UPLOADED, SCANNING, INFECTED, DELETED

    @Column(name = "storage_type")
    private String storageType; // LOCAL, S3, OSS

    @Column(name = "bucket_name")
    private String bucketName;

    @Column(name = "object_key")
    private String objectKey;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "description")
    private String description;

    @Column(name = "tags")
    private String tags;
}
