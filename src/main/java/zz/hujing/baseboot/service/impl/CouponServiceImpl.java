package zz.hujing.baseboot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zz.hujing.baseboot.domain.Coupon;
import zz.hujing.baseboot.domain.Member;
import zz.hujing.baseboot.domain.MemberCoupon;
import zz.hujing.baseboot.repository.CouponRepository;
import zz.hujing.baseboot.repository.MemberCouponRepository;
import zz.hujing.baseboot.repository.MemberRepository;
import zz.hujing.baseboot.service.CouponService;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 优惠券服务实现类
 */
@Slf4j
@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Coupon findById(Long id) {
        return couponRepository.findById(id).orElse(null);
    }

    @Override
    public List<Coupon> findByPage(Map<String, Object> params) {
        int page = params.getOrDefault("page", 1) != null ? (int) params.get("page") : 1;
        int size = params.getOrDefault("size", 10) != null ? (int) params.get("size") : 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Specification<Coupon> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 根据优惠券名称查询
            if (params.containsKey("name") && params.get("name") != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + params.get("name") + "%"));
            }
            // 根据优惠券类型查询
            if (params.containsKey("type") && params.get("type") != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), params.get("type")));
            }
            // 根据状态查询
            if (params.containsKey("status") && params.get("status") != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), params.get("status")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Coupon> couponPage = couponRepository.findAll(specification, pageable);
        return couponPage.getContent();
    }

    @Override
    @Transactional
    public Coupon save(Coupon coupon) {
        // 默认状态为待审核
        if (coupon.getStatus() == null) {
            coupon.setStatus(0);
        }
        // 默认已领取数量为0
        if (coupon.getReceivedCount() == null) {
            coupon.setReceivedCount(0);
        }
        // 默认每人限领1张
        if (coupon.getLimitPerPerson() == null) {
            coupon.setLimitPerPerson(1);
        }
        return couponRepository.save(coupon);
    }

    @Override
    @Transactional
    public Coupon update(Coupon coupon) {
        Coupon existingCoupon = couponRepository.findById(coupon.getId()).orElse(null);
        if (existingCoupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        // 更新优惠券信息
        existingCoupon.setName(coupon.getName());
        existingCoupon.setType(coupon.getType());
        existingCoupon.setValue(coupon.getValue());
        existingCoupon.setMinConsume(coupon.getMinConsume());
        existingCoupon.setCategoryId(coupon.getCategoryId());
        existingCoupon.setTotalCount(coupon.getTotalCount());
        existingCoupon.setLimitPerPerson(coupon.getLimitPerPerson());
        existingCoupon.setStartTime(coupon.getStartTime());
        existingCoupon.setEndTime(coupon.getEndTime());
        existingCoupon.setDescription(coupon.getDescription());
        return couponRepository.save(existingCoupon);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        couponRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void audit(Long id, Integer status, String remark) {
        Coupon coupon = couponRepository.findById(id).orElse(null);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        // 只有待审核的优惠券才能被审核
        if (coupon.getStatus() != 0) {
            throw new RuntimeException("优惠券已审核");
        }
        coupon.setStatus(status);
        couponRepository.save(coupon);
        log.info("优惠券{}审核结果：{}", coupon.getName(), status == 1 ? "通过" : "拒绝");
    }

    @Override
    @Transactional
    public MemberCoupon receiveCoupon(Long memberId, Long couponId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            throw new RuntimeException("会员不存在");
        }

        Coupon coupon = couponRepository.findById(couponId).orElse(null);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }

        // 检查优惠券状态
        if (coupon.getStatus() != 1) {
            throw new RuntimeException("优惠券不可领取");
        }

        // 检查优惠券有效期
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            throw new RuntimeException("优惠券不在有效期内");
        }

        // 检查优惠券剩余数量
        if (coupon.getReceivedCount() >= coupon.getTotalCount()) {
            throw new RuntimeException("优惠券已领完");
        }

        // 检查会员是否已经领取过该优惠券
        Long receivedCount = memberCouponRepository.countByMemberIdAndCouponId(memberId, couponId);
        if (receivedCount >= coupon.getLimitPerPerson()) {
            throw new RuntimeException("您已经领取过该优惠券");
        }

        // 领取优惠券
        MemberCoupon memberCoupon = new MemberCoupon();
        memberCoupon.setMemberId(memberId);
        memberCoupon.setCouponId(couponId);
        memberCoupon.setStatus(0); // 未使用
        memberCouponRepository.save(memberCoupon);

        // 更新优惠券已领取数量
        coupon.setReceivedCount(coupon.getReceivedCount() + 1);
        couponRepository.save(coupon);

        log.info("会员{}领取优惠券{}", member.getMemberNo(), coupon.getName());
        return memberCoupon;
    }

    @Override
    @Transactional
    public void useCoupon(Long memberCouponId, Long orderId) {
        MemberCoupon memberCoupon = memberCouponRepository.findById(memberCouponId).orElse(null);
        if (memberCoupon == null) {
            throw new RuntimeException("会员优惠券不存在");
        }

        // 检查优惠券状态
        if (memberCoupon.getStatus() != 0) {
            throw new RuntimeException("优惠券已使用或已过期");
        }

        // 检查优惠券有效期
        Coupon coupon = couponRepository.findById(memberCoupon.getCouponId()).orElse(null);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        if (LocalDateTime.now().isAfter(coupon.getEndTime())) {
            throw new RuntimeException("优惠券已过期");
        }

        // 使用优惠券
        memberCoupon.setStatus(1); // 已使用
        memberCoupon.setUseTime(LocalDateTime.now());
        memberCoupon.setOrderId(orderId);
        memberCouponRepository.save(memberCoupon);

        log.info("会员使用优惠券{}，订单ID：{}", coupon.getName(), orderId);
    }

    @Override
    public List<MemberCoupon> findMemberCoupons(Long memberId, Integer status) {
        if (status == null) {
            return memberCouponRepository.findByMemberIdOrderByReceiveTimeDesc(memberId);
        } else {
            return memberCouponRepository.findByMemberIdAndStatusOrderByReceiveTimeDesc(memberId, status);
        }
    }

    @Override
    public List<Coupon> findAvailableCoupons() {
        LocalDateTime now = LocalDateTime.now();
        return couponRepository.findByStatusAndEndTimeAfterOrderByCreateTimeDesc(1, now);
    }

    @Override
    public List<Coupon> findAvailableCouponsForMember(Long memberId) {
        List<Coupon> availableCoupons = findAvailableCoupons();
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return availableCoupons;
        }

        // 过滤掉会员已经领取过的优惠券
        List<Coupon> result = new ArrayList<>();
        for (Coupon coupon : availableCoupons) {
            Long receivedCount = memberCouponRepository.countByMemberIdAndCouponId(memberId, coupon.getId());
            if (receivedCount < coupon.getLimitPerPerson()) {
                result.add(coupon);
            }
        }
        return result;
    }
}
