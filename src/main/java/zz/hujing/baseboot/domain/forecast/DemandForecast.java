package zz.hujing.baseboot.domain.forecast;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "demand_forecast")
public class DemandForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "forecast_date", nullable = false)
    private LocalDate forecastDate;

    @Column(name = "forecast_period", nullable = false)
    private Integer forecastPeriod;

    @Column(name = "forecast_quantity", nullable = false)
    private Integer forecastQuantity;

    @Column(name = "forecast_method", nullable = false)
    private String forecastMethod;

    @Column(name = "accuracy")
    private Double accuracy;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();

    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime = LocalDateTime.now();
}