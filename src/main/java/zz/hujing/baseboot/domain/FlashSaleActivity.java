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
 * 限时折扣活动实体类
 */
@Data
@Entity
@Table(name = "flash_sale_activity")
@EntityListeners(AuditingEntityListener.class)
public class FlashSaleActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 活动名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 活动类型（0：秒杀，1：闪购）
     */
    @Column(nullable = false)
    private Integer type;

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
     * 活动状态（0：待审核，1：审核通过，2：审核拒绝，3：已结束）
     */
    @Column(nullable = false)
    private Integer status;

    /**
     * 活动描述
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
     * 活动商品列表
     */
    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private List<FlashSaleActivityProduct> activityProducts;
}
