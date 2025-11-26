package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员优惠券实体类
 */
@Data
@Entity
@Table(name = "member_coupon")
@EntityListeners(AuditingEntityListener.class)
public class MemberCoupon implements Serializable {
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
     * 优惠券ID
     */
    @Column(nullable = false)
    private Long couponId;

    /**
     * 领取时间
     */
    @CreatedDate
    private LocalDateTime receiveTime;

    /**
     * 使用时间
     */
    private LocalDateTime useTime;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 状态（0：未使用，1：已使用，2：已过期）
     */
    @Column(nullable = false)
    private Integer status;

    /**
     * 会员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", insertable = false, updatable = false)
    private Member member;

    /**
     * 优惠券
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couponId", insertable = false, updatable = false)
    private Coupon coupon;
}
