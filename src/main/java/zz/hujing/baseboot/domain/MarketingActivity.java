package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zz.hujing.baseboot.domain.enums.ActivityTypeEnum;
import zz.hujing.baseboot.domain.enums.MemberLevelEnum;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 营销活动
 */
@Data
@Entity
@Table(name = "marketing_activity")
@EntityListeners(AuditingEntityListener.class)
public class MarketingActivity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 活动名称
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * 活动描述
     */
    private String description;
    
    /**
     * 活动类型
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private ActivityTypeEnum type;
    
    /**
     * 适用商品ID（多个用逗号分隔）
     */
    private String productIds;
    
    /**
     * 适用品类ID（多个用逗号分隔）
     */
    private String categoryIds;
    
    /**
     * 折扣比例（如85表示85折）
     */
    private BigDecimal discountRate;
    
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    
    /**
     * 会员等级限制（最低会员等级）
     */
    @Enumerated(EnumType.ORDINAL)
    private MemberLevelEnum minMemberLevel;
    
    /**
     * 每人限购数量
     */
    private Integer limitPerUser;
    
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
     * 状态（1：待审核，2：已审核，3：已结束，4：已取消）
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