package zz.hujing.baseboot.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "sales_record")
public class SalesRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 产品ID
     */
    @Column(name = "product_id", nullable = false)
    private Long productId;

    /**
     * 销售数量
     */
    @Column(name = "sales_quantity", nullable = false)
    private Integer salesQuantity;

    /**
     * 销售单价
     */
    @Column(name = "sales_price", nullable = false)
    private BigDecimal salesPrice;

    /**
     * 销售金额
     */
    @Column(name = "sales_amount", nullable = false)
    private BigDecimal salesAmount;

    /**
     * 销售日期
     */
    @Column(name = "sales_date", nullable = false)
    private LocalDateTime salesDate;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
}
