package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.CouponTemplate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券模板Repository
 */
@Repository
public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long> {
    
    /**
     * 查询可领取的优惠券模板列表
     * @param status 状态（2：已审核）
     * @param startTime 开始时间小于等于当前时间
     * @param endTime 结束时间大于当前时间
     * @return 可领取的优惠券模板列表
     */
    List<CouponTemplate> findByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThan(Integer status, LocalDateTime startTime, LocalDateTime endTime);
}