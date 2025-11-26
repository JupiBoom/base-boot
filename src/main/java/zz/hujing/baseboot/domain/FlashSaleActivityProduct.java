package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 限时折扣活动商品实体类
 */
@Data
@Entity
@Table(name = "flash_sale_activity_product")
@EntityListeners(AuditingEntityListener.class)
public class FlashSaleActivityProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 活动ID
     */
    @Column(nullable = false)
    private Long activityId;

    /**
     * 商品ID
     */
    @Column(nullable = false)
    private Long productId;

    /**
     * 商品名称
     */
    @Column(nullable = false)
    private String productName;

    /**
     * 原价
     */
    @Column(nullable = false)
    private BigDecimal originalPrice;

    /**
     * 活动价
     */
    @Column(nullable = false)
    private BigDecimal activityPrice;

    /**
     * 活动库存
     */
    @Column(nullable = false)
    private Integer activityStock;

    /**
     * 已售数量
     */
    @Column(nullable = false)
    private Integer soldCount;

    /**
     * 每人限购数量
     */
    @Column(nullable = false)
    private Integer limitPerPerson;

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
     * 活动
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activityId", insertable = false, updatable = false)
    private FlashSaleActivity activity;
}
