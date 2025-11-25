package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zz.hujing.baseboot.domain.enums.UserGroupTypeEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户分群
 */
@Data
@Entity
@Table(name = "user_group")
@EntityListeners(AuditingEntityListener.class)
public class UserGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 分群名称
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * 分群描述
     */
    private String description;
    
    /**
     * 分群类型
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserGroupTypeEnum type;
    
    /**
     * 分群规则（JSON格式存储）
     */
    @Column(nullable = false)
    private String rule;
    
    /**
     * 分群用户数量
     */
    private Integer userCount = 0;
    
    /**
     * 状态（1：启用，0：禁用）
     */
    @Column(nullable = false)
    private Integer status = 1;
    
    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @LastModifiedDate
    private LocalDateTime updateTime;
}