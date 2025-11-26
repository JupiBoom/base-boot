package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员等级实体类
 */
@Data
@Entity
@Table(name = "member_level")
@EntityListeners(AuditingEntityListener.class)
public class MemberLevel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 等级名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 等级代码（common/silver/gold/diamond）
     */
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * 最低成长值
     */
    @Column(nullable = false)
    private Integer minGrowthValue;

    /**
     * 最高成长值
     */
    private Integer maxGrowthValue;

    /**
     * 等级权益描述
     */
    private String benefitDescription;

    /**
     * 状态（0：启用，1：禁用）
     */
    @Column(nullable = false)
    private Integer status;

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

    /**
     * 会员列表
     */
    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    private List<Member> members;

    /**
     * 会员等级变更记录
     */
    @OneToMany(mappedBy = "newLevel", fetch = FetchType.LAZY)
    private List<MemberLevelChangeRecord> levelChangeRecords;
}
