package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.AlertRecord;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预警记录仓库接口
 */
@Repository
public interface AlertRecordRepository extends JpaRepository<AlertRecord, Long> {

    /**
     * 根据产品ID查询预警记录
     * @param productId 产品ID
     * @return 预警记录列表
     */
    List<AlertRecord> findByProductId(Long productId);

    /**
     * 根据预警类型查询预警记录
     * @param alertType 预警类型
     * @return 预警记录列表
     */
    List<AlertRecord> findByAlertType(Integer alertType);

    /**
     * 根据预警级别查询预警记录
     * @param alertLevel 预警级别
     * @return 预警记录列表
     */
    List<AlertRecord> findByAlertLevel(Integer alertLevel);

    /**
     * 根据处理状态查询预警记录
     * @param handleStatus 处理状态
     * @return 预警记录列表
     */
    List<AlertRecord> findByHandleStatus(Integer handleStatus);

    /**
     * 根据创建时间范围查询预警记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预警记录列表
     */
    List<AlertRecord> findByCreateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
