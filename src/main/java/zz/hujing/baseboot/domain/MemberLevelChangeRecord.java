package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员等级变更记录实体类
 */
@Data
@Entity
@Table(name = "member_level_change_record")
@EntityListeners(AuditingEntityListener.class)
public class MemberLevelChangeRecord implements Serializable {
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
     * 原等级ID
     */
    @Column(nullable = false)
    private Long oldLevelId;

    /**
     * 新等级ID
     */
    @Column(nullable = false)
    private Long newLevelId;

    /**
     * 变更原因
     */
    @Column(nullable = false)
    private String changeReason;

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

    /**
     * 原等级
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oldLevelId", insertable = false, updatable = false)
    private MemberLevel oldLevel;

    /**
     * 新等级
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newLevelId", insertable = false, updatable = false)
    private MemberLevel newLevel;
}
