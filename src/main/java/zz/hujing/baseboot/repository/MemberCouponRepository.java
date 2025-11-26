package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.MemberCoupon;

import java.util.List;

/**
 * 会员优惠券Repository
 */
@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long>, JpaSpecificationExecutor<MemberCoupon> {
    /**
     * 根据会员ID查询优惠券
     * @param memberId 会员ID
     * @return 会员优惠券列表
     */
    List<MemberCoupon> findByMemberIdOrderByReceiveTimeDesc(Long memberId);

    /**
     * 根据会员ID和优惠券ID查询
     * @param memberId 会员ID
     * @param couponId 优惠券ID
     * @return 会员优惠券信息
     */
    MemberCoupon findByMemberIdAndCouponId(Long memberId, Long couponId);

    /**
     * 根据会员ID和状态查询优惠券
     * @param memberId 会员ID
     * @param status 状态
     * @return 会员优惠券列表
     */
    List<MemberCoupon> findByMemberIdAndStatusOrderByReceiveTimeDesc(Long memberId, Integer status);

    /**
     * 查询会员领取某张优惠券的数量
     * @param memberId 会员ID
     * @param couponId 优惠券ID
     * @return 领取数量
     */
    Long countByMemberIdAndCouponId(Long memberId, Long couponId);
}
