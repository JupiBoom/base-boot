package zz.hujing.baseboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zz.hujing.baseboot.domain.CouponTemplate;
import zz.hujing.baseboot.domain.UserCoupon;
import zz.hujing.baseboot.domain.enums.CouponStatusEnum;
import zz.hujing.baseboot.repository.CouponTemplateRepository;
import zz.hujing.baseboot.repository.UserCouponRepository;
import zz.hujing.baseboot.service.CouponService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 优惠券服务实现类
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    
    private final CouponTemplateRepository couponTemplateRepository;
    private final UserCouponRepository userCouponRepository;
    
    @Override
    public CouponTemplate findCouponTemplateById(Long id) {
        return couponTemplateRepository.findById(id).orElse(null);
    }
    
    @Override
    public CouponTemplate saveCouponTemplate(CouponTemplate couponTemplate) {
        // 初始化已发放数量为0
        couponTemplate.setIssuedQuantity(0);
        // 设置状态为待审核
        couponTemplate.setStatus(1);
        return couponTemplateRepository.save(couponTemplate);
    }
    
    @Override
    public void auditCouponTemplate(Long id, Integer status) {
        CouponTemplate couponTemplate = couponTemplateRepository.findById(id).orElseThrow(() -> new RuntimeException("优惠券模板不存在"));
        couponTemplate.setStatus(status);
        couponTemplateRepository.save(couponTemplate);
    }
    
    @Override
    public List<CouponTemplate> findAvailableCouponTemplates() {
        LocalDateTime now = LocalDateTime.now();
        // 查询已审核、开始领取时间小于等于当前时间、结束领取时间大于当前时间的优惠券模板
        return couponTemplateRepository.findByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThan(2, now, now);
    }
    
    @Override
    @Transactional
    public UserCoupon receiveCoupon(Long memberId, Long couponTemplateId) {
        CouponTemplate couponTemplate = couponTemplateRepository.findById(couponTemplateId).orElseThrow(() -> new RuntimeException("优惠券模板不存在"));
        
        // 检查优惠券模板状态是否为已审核
        if (couponTemplate.getStatus() != 2) {
            throw new RuntimeException("优惠券模板未审核通过");
        }
        
        // 检查领取时间是否在有效期内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(couponTemplate.getStartTime()) || now.isAfter(couponTemplate.getEndTime())) {
            throw new RuntimeException("优惠券领取时间已过期");
        }
        
        // 检查是否还有剩余数量
        if (couponTemplate.getIssuedQuantity() >= couponTemplate.getTotalQuantity()) {
            throw new RuntimeException("优惠券已领完");
        }
        
        // 检查用户是否已经领取过该优惠券
        Integer receivedCount = userCouponRepository.countByMemberIdAndCouponTemplateId(memberId, couponTemplateId);
        if (receivedCount >= couponTemplate.getLimitPerUser()) {
            throw new RuntimeException("您已经领取过该优惠券");
        }
        
        // 生成用户优惠券
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setMemberId(memberId);
        userCoupon.setCouponTemplateId(couponTemplateId);
        userCoupon.setCouponNo(generateCouponNo());
        userCoupon.setStatus(CouponStatusEnum.UNUSED);
        userCoupon.setExpireTime(now.plusDays(couponTemplate.getValidDays()));
        
        // 保存用户优惠券
        userCoupon = userCouponRepository.save(userCoupon);
        
        // 更新优惠券模板的已发放数量
        couponTemplate.setIssuedQuantity(couponTemplate.getIssuedQuantity() + 1);
        couponTemplateRepository.save(couponTemplate);
        
        return userCoupon;
    }
    
    @Override
    public List<UserCoupon> findUserCoupons(Long memberId, Integer status) {
        if (status == null) {
            return userCouponRepository.findAll();
        } else {
            CouponStatusEnum couponStatus = CouponStatusEnum.values()[status - 1];
            return userCouponRepository.findByMemberIdAndStatus(memberId, couponStatus);
        }
    }
    
    @Override
    @Transactional
    public void useCoupon(Long userCouponId, Long orderId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow(() -> new RuntimeException("用户优惠券不存在"));
        
        // 检查优惠券状态是否为未使用
        if (!userCoupon.getStatus().equals(CouponStatusEnum.UNUSED)) {
            throw new RuntimeException("优惠券已使用或已过期");
        }
        
        // 检查优惠券是否已过期
        if (LocalDateTime.now().isAfter(userCoupon.getExpireTime())) {
            userCoupon.setStatus(CouponStatusEnum.EXPIRED);
            userCouponRepository.save(userCoupon);
            throw new RuntimeException("优惠券已过期");
        }
        
        // 使用优惠券
        userCoupon.setStatus(CouponStatusEnum.USED);
        userCoupon.setUseTime(LocalDateTime.now());
        userCoupon.setOrderId(orderId);
        userCouponRepository.save(userCoupon);
    }
    
    /**
     * 生成优惠券编号
     * @return 优惠券编号
     */
    private String generateCouponNo() {
        // 简单生成规则：前缀+时间戳+随机数
        return "COUP" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}