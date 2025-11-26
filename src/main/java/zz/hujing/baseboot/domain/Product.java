package zz.hujing.baseboot.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 产品编码
     */
    @Column(name = "product_code", unique = true, nullable = false)
    private String productCode;

    /**
     * 产品名称
     */
    @Column(name = "product_name", nullable = false)
    private String productName;

    /**
     * 产品描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 安全库存阈值
     */
    @Column(name = "safety_stock", nullable = false)
    private Integer safetyStock;

    /**
     * 单位
     */
    @Column(name = "unit", nullable = false)
    private String unit;

    /**
     * 成本价
     */
    @Column(name = "cost_price", nullable = false)
    private BigDecimal costPrice;

    /**
     * 销售价
     */
    @Column(name = "sale_price", nullable = false)
    private BigDecimal salePrice;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    /**
     * 状态（0：禁用，1：启用）
     */
    @Column(name = "status", nullable = false)
    private Integer status;
}
