package zz.hujing.baseboot.domain.enums;

import lombok.Getter;

/**
 * 优惠券状态枚举
 */
@Getter
public enum CouponStatusEnum {
    
    UNUSED(1, "未使用"),
    USED(2, "已使用"),
    EXPIRED(3, "已过期");
    
    private final Integer status;
    private final String name;
    
    CouponStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }
}