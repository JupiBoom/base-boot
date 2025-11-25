package zz.hujing.baseboot.domain.enums;

import lombok.Getter;

/**
 * 优惠券类型枚举
 */
@Getter
public enum CouponTypeEnum {
    
    FULL_REDUCTION(1, "满减券"),
    DISCOUNT(2, "折扣券"),
    CATEGORY(3, "品类券");
    
    private final Integer type;
    private final String name;
    
    CouponTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
}