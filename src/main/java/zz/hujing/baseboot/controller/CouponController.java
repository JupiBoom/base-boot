package zz.hujing.baseboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.domain.Coupon;
import zz.hujing.baseboot.domain.MemberCoupon;
import zz.hujing.baseboot.service.CouponService;

import java.util.List;
import java.util.Map;

/**
 * 优惠券管理Controller
 */
@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    /**
     * 根据ID查询优惠券
     */
    @GetMapping("/{id}")
    public Coupon findById(@PathVariable Long id) {
        return couponService.findById(id);
    }

    /**
     * 分页查询优惠券
     */
    @GetMapping("/page")
    public List<Coupon> findByPage(@RequestParam Map<String, Object> params) {
        return couponService.findByPage(params);
    }

    /**
     * 保存优惠券
     */
    @PostMapping
    public Coupon save(@RequestBody Coupon coupon) {
        return couponService.save(coupon);
    }

    /**
     * 更新优惠券
     */
    @PutMapping
    public Coupon update(@RequestBody Coupon coupon) {
        return couponService.update(coupon);
    }

    /**
     * 删除优惠券
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        couponService.delete(id);
    }

    /**
     * 审核优惠券
     */
    @PutMapping("/audit/{id}")
    public void audit(@PathVariable Long id, @RequestParam Integer status, @RequestParam(required = false) String remark) {
        couponService.audit(id, status, remark);
    }

    /**
     * 会员领取优惠券
     */
    @PostMapping("/receive/{memberId}/{couponId}")
    public MemberCoupon receiveCoupon(@PathVariable Long memberId, @PathVariable Long couponId) {
        return couponService.receiveCoupon(memberId, couponId);
    }

    /**
     * 会员使用优惠券
     */
    @PutMapping("/use/{memberCouponId}")
    public void useCoupon(@PathVariable Long memberCouponId, @RequestParam Long orderId) {
        couponService.useCoupon(memberCouponId, orderId);
    }

    /**
     * 查询会员优惠券
     */
    @GetMapping("/memberCoupons/{memberId}")
    public List<MemberCoupon> findMemberCoupons(@PathVariable Long memberId, @RequestParam(required = false) Integer status) {
        return couponService.findMemberCoupons(memberId, status);
    }

    /**
     * 查询可用优惠券
     */
    @GetMapping("/available")
    public List<Coupon> findAvailableCoupons() {
        return couponService.findAvailableCoupons();
    }

    /**
     * 查询会员可领取的优惠券
     */
    @GetMapping("/availableForMember/{memberId}")
    public List<Coupon> findAvailableCouponsForMember(@PathVariable Long memberId) {
        return couponService.findAvailableCouponsForMember(memberId);
    }
}
