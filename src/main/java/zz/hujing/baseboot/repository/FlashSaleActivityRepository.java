package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.FlashSaleActivity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 限时折扣活动Repository
 */
@Repository
public interface FlashSaleActivityRepository extends JpaRepository<FlashSaleActivity, Long>, JpaSpecificationExecutor<FlashSaleActivity> {
    /**
     * 查询审核通过且正在进行中的活动
     * @param status 状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 活动列表
     */
    List<FlashSaleActivity> findByStatusAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(Integer status, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询审核通过的活动（包括未开始、进行中、已结束）
     * @param status 状态
     * @return 活动列表
     */
    List<FlashSaleActivity> findByStatusOrderByStartTimeDesc(Integer status);
}
