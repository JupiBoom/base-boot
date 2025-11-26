package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员专享活动实体类
 */
@Data
@Entity
@Table(name = "member_exclusive_activity")
@EntityListeners(AuditingEntityListener.class)
public class MemberExclusiveActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 活动名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 适用会员等级ID（多个用逗号分隔，为空表示所有等级）
     */
    private String applicableLevelIds;

    /**
     * 活动开始时间
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @Column(nullable = false)
    private LocalDateTime endTime;

    /**
     * 活动状态（0：待审核，1：审核通过，2：审核拒绝，3：已结束）
     */
    @Column(nullable = false)
    private Integer status;

    /**
     * 活动描述
     */
    private String description;

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
