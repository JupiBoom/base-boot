package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.MarketingActivity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 营销活动Repository
 */
@Repository
public interface MarketingActivityRepository extends JpaRepository<MarketingActivity, Long> {
    
    /**
     * 查询正在进行中的营销活动列表
     * @param status 状态（2：已审核）
     * @param startTime 开始时间小于等于当前时间
     * @param endTime 结束时间大于当前时间
     * @return 正在进行中的营销活动列表
     */
    List<MarketingActivity> findByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThan(Integer status, LocalDateTime startTime, LocalDateTime endTime);
}