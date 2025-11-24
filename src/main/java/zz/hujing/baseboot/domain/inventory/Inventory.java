package zz.hujing.baseboot.domain.inventory;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Column(name = "available_stock", nullable = false)
    private Integer availableStock;

    @Column(name = "reserved_stock", nullable = false)
    private Integer reservedStock = 0;

    @Column(name = "in_transit_stock", nullable = false)
    private Integer inTransitStock = 0;

    @Column(name = "last_stock_update_time", nullable = false)
    private LocalDateTime lastStockUpdateTime = LocalDateTime.now();

    @Column(name = "last_purchase_time")
    private LocalDateTime lastPurchaseTime;

    @Column(name = "last_sale_time")
    private LocalDateTime lastSaleTime;
}