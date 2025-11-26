package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员成长值记录实体类
 */
@Data
@Entity
@Table(name = "member_growth_record")
@EntityListeners(AuditingEntityListener.class)
public class MemberGrowthRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会员ID
     */
    @Column(nullable = false)
    private Long memberId;

    /**
     * 变更类型（0：消费增加，1：活跃度增加，2：系统调整）
     */
    @Column(nullable = false)
    private Integer changeType;

    /**
     * 变更值
     */
    @Column(nullable = false)
    private Integer changeValue;

    /**
     * 变更后成长值
     */
    @Column(nullable = false)
    private Integer afterGrowthValue;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;

    /**
     * 会员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", insertable = false, updatable = false)
    private Member member;
}
