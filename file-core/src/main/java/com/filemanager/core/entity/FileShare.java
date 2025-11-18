package com.filemanager.core.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "file_share")
@EntityListeners(AuditingEntityListener.class)
public class FileShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @Column(name = "shared_by", nullable = false)
    private Long sharedBy;

    @Column(name = "shared_to")
    private Long sharedTo;

    @Column(name = "share_type")
    private String shareType; // PUBLIC, PRIVATE

    @Column(name = "share_token")
    private String shareToken;

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    @Column(name = "permission")
    private String permission; // READ, WRITE

    @Column(name = "created_time")
    @CreatedDate
    private LocalDateTime createdTime;

    @Column(name = "status")
    private String status; // ACTIVE, EXPIRED, REVOKED
}
