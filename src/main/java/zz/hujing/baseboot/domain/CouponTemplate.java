package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zz.hujing.baseboot.domain.enums.CouponTypeEnum;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券模板
 */
@Data
@Entity
@Table(name = "coupon_template")
@EntityListeners(AuditingEntityListener.class)
public class CouponTemplate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 优惠券名称
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * 优惠券描述
     */
    private String description;
    
    /**
     * 优惠券类型
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private CouponTypeEnum type;
    
    /**
     * 面值（满减券：减免金额；折扣券：折扣比例，如85表示85折；品类券：减免金额）
     */
    @Column(nullable = false)
    private BigDecimal faceValue;
    
    /**
     * 最低使用金额（满减券和品类券需要设置）
     */
    private BigDecimal minOrderAmount;
    
    /**
     * 适用品类ID（品类券需要设置）
     */
    private Long categoryId;
    
    /**
     * 每人限领数量
     */
    @Column(nullable = false)
    private Integer limitPerUser = 1;
    
    /**
     * 总发放数量
     */
    @Column(nullable = false)
    private Integer totalQuantity;
    
    /**
     * 已发放数量
     */
    private Integer issuedQuantity = 0;
    
    /**
     * 开始领取时间
     */
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    /**
     * 结束领取时间
     */
    @Column(nullable = false)
    private LocalDateTime endTime;
    
    /**
     * 有效期天数（自领取之日起）
     */
    @Column(nullable = false)
    private Integer validDays;
    
    /**
     * 状态（1：待审核，2：已审核，3：已过期，4：已下架）
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