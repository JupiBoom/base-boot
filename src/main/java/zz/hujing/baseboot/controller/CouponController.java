package zz.hujing.baseboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.core.result.CommonResult;
import zz.hujing.baseboot.domain.CouponTemplate;
import zz.hujing.baseboot.domain.UserCoupon;
import zz.hujing.baseboot.service.CouponService;

import java.util.List;

/**
 * 优惠券控制器
 */
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {
    
    private final CouponService couponService;
    
    /**
     * 根据ID查询优惠券模板
     * @param id 优惠券模板ID
     * @return 优惠券模板信息
     */
    @GetMapping("/template/{id}")
    public CommonResult<CouponTemplate> findCouponTemplateById(@PathVariable Long id) {
        CouponTemplate couponTemplate = couponService.findCouponTemplateById(id);
        return CommonResult.success(couponTemplate);
    }
    
    /**
     * 保存优惠券模板
     * @param couponTemplate 优惠券模板信息
     * @return 保存后的优惠券模板信息
     */
    @PostMapping("/template")
    public CommonResult<CouponTemplate> saveCouponTemplate(@RequestBody CouponTemplate couponTemplate) {
        CouponTemplate savedTemplate = couponService.saveCouponTemplate(couponTemplate);
        return CommonResult.success(savedTemplate);
    }
    
    /**
     * 审核优惠券模板
     * @param id 优惠券模板ID
     * @param status 审核状态
     * @return 操作结果
     */
    @PutMapping("/template/audit")
    public CommonResult<Void> auditCouponTemplate(@RequestParam Long id, @RequestParam Integer status) {
        couponService.auditCouponTemplate(id, status);
        return CommonResult.success();
    }
    
    /**
     * 查询可领取的优惠券模板列表
     * @return 可领取的优惠券模板列表
     */
    @GetMapping("/template/available")
    public CommonResult<List<CouponTemplate>> findAvailableCouponTemplates() {
        List<CouponTemplate> templateList = couponService.findAvailableCouponTemplates();
        return CommonResult.success(templateList);
    }
    
    /**
     * 用户领取优惠券
     * @param memberId 会员ID
     * @param couponTemplateId 优惠券模板ID
     * @return 领取的优惠券信息
     */
    @PostMapping("/receive")
    public CommonResult<UserCoupon> receiveCoupon(@RequestParam Long memberId, @RequestParam Long couponTemplateId) {
        UserCoupon userCoupon = couponService.receiveCoupon(memberId, couponTemplateId);
        return CommonResult.success(userCoupon);
    }
    
    /**
     * 查询用户的优惠券列表
     * @param memberId 会员ID
     * @param status 优惠券状态（可选）
     * @return 用户的优惠券列表
     */
    @GetMapping("/user")
    public CommonResult<List<UserCoupon>> findUserCoupons(@RequestParam Long memberId, @RequestParam(required = false) Integer status) {
        List<UserCoupon> userCouponList = couponService.findUserCoupons(memberId, status);
        return CommonResult.success(userCouponList);
    }
    
    /**
     * 使用优惠券
     * @param userCouponId 用户优惠券ID
     * @param orderId 订单ID
     * @return 操作结果
     */
    @PutMapping("/use")
    public CommonResult<Void> useCoupon(@RequestParam Long userCouponId, @RequestParam Long orderId) {
        couponService.useCoupon(userCouponId, orderId);
        return CommonResult.success();
    }
}