package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员实体类
 */
@Data
@Entity
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会员编号
     */
    @Column(unique = true, nullable = false)
    private String memberNo;

    /**
     * 会员姓名
     */
    private String name;

    /**
     * 会员手机号
     */
    @Column(unique = true)
    private String phone;

    /**
     * 会员邮箱
     */
    private String email;

    /**
     * 会员等级ID
     */
    @Column(nullable = false)
    private Long levelId;

    /**
     * 当前成长值
     */
    @Column(nullable = false)
    private Integer growthValue;

    /**
     * 总消费金额
     */
    @Column(nullable = false)
    private BigDecimal totalConsume;

    /**
     * 活跃度
     */
    @Column(nullable = false)
    private Integer activity;

    /**
     * 偏好标签（逗号分隔）
     */
    private String preferenceTags;

    /**
     * 注册时间
     */
    @CreatedDate
    private LocalDateTime registerTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 状态（0：正常，1：冻结）
     */
    @Column(nullable = false)
    private Integer status;

    /**
     * 会员等级
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "levelId", insertable = false, updatable = false)
    private MemberLevel level;

    /**
     * 会员成长值记录
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberGrowthRecord> growthRecords;

    /**
     * 会员等级变更记录
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberLevelChangeRecord> levelChangeRecords;

    /**
     * 会员优惠券
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberCoupon> memberCoupons;
}
