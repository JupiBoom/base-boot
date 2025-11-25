package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zz.hujing.baseboot.domain.enums.CouponStatusEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户优惠券
 */
@Data
@Entity
@Table(name = "user_coupon")
@EntityListeners(AuditingEntityListener.class)
public class UserCoupon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 会员ID
     */
    @Column(nullable = false)
    private Long memberId;
    
    /**
     * 优惠券模板ID
     */
    @Column(nullable = false)
    private Long couponTemplateId;
    
    /**
     * 优惠券编号
     */
    @Column(unique = true, nullable = false)
    private String couponNo;
    
    /**
     * 状态
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private CouponStatusEnum status = CouponStatusEnum.UNUSED;
    
    /**
     * 领取时间
     */
    @CreatedDate
    private LocalDateTime receiveTime;
    
    /**
     * 过期时间
     */
    @Column(nullable = false)
    private LocalDateTime expireTime;
    
    /**
     * 使用时间
     */
    private LocalDateTime useTime;
    
    /**
     * 使用订单ID
     */
    private Long orderId;
    
    /**
     * 更新时间
     */
    @LastModifiedDate
    private LocalDateTime updateTime;
}