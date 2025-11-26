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
 * 优惠券实体类
 */
@Data
@Entity
@Table(name = "coupon")
@EntityListeners(AuditingEntityListener.class)
public class Coupon implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 优惠券名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 优惠券类型（0：满减券，1：折扣券，2：品类券）
     */
    @Column(nullable = false)
    private Integer type;

    /**
     * 面值（满减券：减免金额；折扣券：折扣比例，如8.5表示85折）
     */
    @Column(nullable = false)
    private BigDecimal value;

    /**
     * 使用门槛（满多少金额可用）
     */
    private BigDecimal minConsume;

    /**
     * 适用品类ID（品类券用）
     */
    private Long categoryId;

    /**
     * 总发行量
     */
    @Column(nullable = false)
    private Integer totalCount;

    /**
     * 已领取数量
     */
    @Column(nullable = false)
    private Integer receivedCount;

    /**
     * 每人限领数量
     */
    @Column(nullable = false)
    private Integer limitPerPerson;

    /**
     * 有效期开始时间
     */
    @Column(nullable = false)
    private LocalDateTime startTime;

    /**
     * 有效期结束时间
     */
    @Column(nullable = false)
    private LocalDateTime endTime;

    /**
     * 状态（0：待审核，1：审核通过，2：审核拒绝，3：已过期）
     */
    @Column(nullable = false)
    private Integer status;

    /**
     * 描述
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

    /**
     * 会员优惠券列表
     */
    @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY)
    private List<MemberCoupon> memberCoupons;
}
