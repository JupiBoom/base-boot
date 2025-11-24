package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.sales.SalesRecord;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {
    List<SalesRecord> findByProductIdAndSaleDateBetween(Long productId, LocalDate startDate, LocalDate endDate);
    List<SalesRecord> findByProductIdOrderBySaleDateDesc(Long productId);

    @Query("SELECT sr FROM SalesRecord sr WHERE sr.productId = :productId GROUP BY sr.saleDate ORDER BY sr.saleDate DESC")
    List<SalesRecord> findDailySalesByProductId(Long productId);
}