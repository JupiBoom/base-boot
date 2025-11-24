package zz.hujing.baseboot.domain.sales;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "sales_record")
public class SalesRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    @Column(name = "sale_quantity", nullable = false)
    private Integer saleQuantity;

    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "order_number")
    private String orderNumber;
}