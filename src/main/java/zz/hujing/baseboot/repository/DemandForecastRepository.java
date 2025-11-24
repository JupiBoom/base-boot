package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.forecast.DemandForecast;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DemandForecastRepository extends JpaRepository<DemandForecast, Long> {
    List<DemandForecast> findByProductIdAndForecastDateBetween(Long productId, LocalDate startDate, LocalDate endDate);
    List<DemandForecast> findByProductIdOrderByForecastDateDesc(Long productId);
}