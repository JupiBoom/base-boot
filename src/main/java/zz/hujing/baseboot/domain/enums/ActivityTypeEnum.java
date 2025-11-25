package zz.hujing.baseboot.domain.enums;

import lombok.Getter;

/**
 * 活动类型枚举
 */
@Getter
public enum ActivityTypeEnum {
    
    LIMITED_DISCOUNT(1, "限时折扣"),
    FLASH_SALE(2, "闪购"),
    SEckILL(3, "秒杀"),
    MEMBER_EXCLUSIVE(4, "会员专享");
    
    private final Integer type;
    private final String name;
    
    ActivityTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
}