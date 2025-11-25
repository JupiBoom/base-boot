package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.UserCoupon;
import zz.hujing.baseboot.domain.enums.CouponStatusEnum;

import java.util.List;
import java.util.Optional;

/**
 * 用户优惠券Repository
 */
@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    
    /**
     * 根据会员ID和状态查询用户优惠券列表
     * @param memberId 会员ID
     * @param status 优惠券状态
     * @return 用户优惠券列表
     */
    List<UserCoupon> findByMemberIdAndStatus(Long memberId, CouponStatusEnum status);
    
    /**
     * 根据会员ID和优惠券模板ID查询用户优惠券数量
     * @param memberId 会员ID
     * @param couponTemplateId 优惠券模板ID
     * @return 用户优惠券数量
     */
    Integer countByMemberIdAndCouponTemplateId(Long memberId, Long couponTemplateId);
    
    /**
     * 根据ID和会员ID查询用户优惠券
     * @param id 用户优惠券ID
     * @param memberId 会员ID
     * @return 用户优惠券信息
     */
    Optional<UserCoupon> findByIdAndMemberId(Long id, Long memberId);
}