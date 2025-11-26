package zz.hujing.baseboot.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "inventory")
public class Inventory implements Serializable {

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
     * 当前库存数量
     */
    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    /**
     * 入库数量
     */
    @Column(name = "in_stock")
    private Integer inStock;

    /**
     * 出库数量
     */
    @Column(name = "out_stock")
    private Integer outStock;

    /**
     * 库存更新时间
     */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    /**
     * 上次入库时间
     */
    @Column(name = "last_in_time")
    private LocalDateTime lastInTime;

    /**
     * 上次出库时间
     */
    @Column(name = "last_out_time")
    private LocalDateTime lastOutTime;
}
