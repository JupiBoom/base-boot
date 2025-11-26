package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.Coupon;
import zz.hujing.baseboot.domain.MemberCoupon;

import java.util.List;
import java.util.Map;

/**
 * 优惠券服务接口
 */
public interface CouponService {
    /**
     * 根据ID查询优惠券
     * @param id 优惠券ID
     * @return 优惠券信息
     */
    Coupon findById(Long id);

    /**
     * 分页查询优惠券
     * @param params 查询参数
     * @return 优惠券列表
     */
    List<Coupon> findByPage(Map<String, Object> params);

    /**
     * 保存优惠券
     * @param coupon 优惠券信息
     * @return 保存后的优惠券信息
     */
    Coupon save(Coupon coupon);

    /**
     * 更新优惠券
     * @param coupon 优惠券信息
     * @return 更新后的优惠券信息
     */
    Coupon update(Coupon coupon);

    /**
     * 删除优惠券
     * @param id 优惠券ID
     */
    void delete(Long id);

    /**
     * 审核优惠券
     * @param id 优惠券ID
     * @param status 审核状态
     * @param remark 审核备注
     */
    void audit(Long id, Integer status, String remark);

    /**
     * 会员领取优惠券
     * @param memberId 会员ID
     * @param couponId 优惠券ID
     * @return 会员优惠券信息
     */
    MemberCoupon receiveCoupon(Long memberId, Long couponId);

    /**
     * 会员使用优惠券
     * @param memberCouponId 会员优惠券ID
     * @param orderId 订单ID
     */
    void useCoupon(Long memberCouponId, Long orderId);

    /**
     * 查询会员优惠券
     * @param memberId 会员ID
     * @param status 状态
     * @return 会员优惠券列表
     */
    List<MemberCoupon> findMemberCoupons(Long memberId, Integer status);

    /**
     * 查询可用优惠券
     * @return 可用优惠券列表
     */
    List<Coupon> findAvailableCoupons();

    /**
     * 查询会员可领取的优惠券
     * @param memberId 会员ID
     * @return 可领取优惠券列表
     */
    List<Coupon> findAvailableCouponsForMember(Long memberId);
}
