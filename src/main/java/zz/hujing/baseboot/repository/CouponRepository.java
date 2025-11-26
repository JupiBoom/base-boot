package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.Coupon;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券Repository
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {
    /**
     * 查询审核通过且未过期的优惠券
     * @param status 状态
     * @param endTime 结束时间
     * @return 优惠券列表
     */
    List<Coupon> findByStatusAndEndTimeAfterOrderByCreateTimeDesc(Integer status, LocalDateTime endTime);
}
