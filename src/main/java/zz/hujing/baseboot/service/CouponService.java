package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.CouponTemplate;
import zz.hujing.baseboot.domain.UserCoupon;

import java.util.List;

/**
 * 优惠券服务接口
 */
public interface CouponService {
    
    /**
     * 根据ID查询优惠券模板
     * @param id 优惠券模板ID
     * @return 优惠券模板信息
     */
    CouponTemplate findCouponTemplateById(Long id);
    
    /**
     * 保存优惠券模板
     * @param couponTemplate 优惠券模板信息
     * @return 保存后的优惠券模板信息
     */
    CouponTemplate saveCouponTemplate(CouponTemplate couponTemplate);
    
    /**
     * 审核优惠券模板
     * @param id 优惠券模板ID
     * @param status 审核状态（2：通过，4：拒绝）
     */
    void auditCouponTemplate(Long id, Integer status);
    
    /**
     * 查询可领取的优惠券模板列表
     * @return 可领取的优惠券模板列表
     */
    List<CouponTemplate> findAvailableCouponTemplates();
    
    /**
     * 用户领取优惠券
     * @param memberId 会员ID
     * @param couponTemplateId 优惠券模板ID
     * @return 领取的优惠券信息
     */
    UserCoupon receiveCoupon(Long memberId, Long couponTemplateId);
    
    /**
     * 查询用户的优惠券列表
     * @param memberId 会员ID
     * @param status 优惠券状态（可选）
     * @return 用户的优惠券列表
     */
    List<UserCoupon> findUserCoupons(Long memberId, Integer status);
    
    /**
     * 使用优惠券
     * @param userCouponId 用户优惠券ID
     * @param orderId 订单ID
     */
    void useCoupon(Long userCouponId, Long orderId);
}