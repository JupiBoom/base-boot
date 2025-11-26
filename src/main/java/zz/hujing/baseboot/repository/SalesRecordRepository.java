package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.SalesRecord;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 销售记录仓库接口
 */
@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {

    /**
     * 根据产品ID和销售日期范围查询销售记录
     * @param productId 产品ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 销售记录列表
     */
    List<SalesRecord> findByProductIdAndSalesDateBetween(Long productId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 根据销售日期范围查询销售记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 销售记录列表
     */
    List<SalesRecord> findBySalesDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查询指定产品最近N天的销售记录
     * @param productId 产品ID
     * @param days 天数
     * @return 销售记录列表
     */
    @Query("SELECT s FROM SalesRecord s WHERE s.productId = :productId AND s.salesDate >= :startDate ORDER BY s.salesDate DESC")
    List<SalesRecord> findRecentSalesByProductId(@Param("productId") Long productId, @Param("startDate") LocalDateTime startDate);
}
