package zz.hujing.baseboot.domain.enums;

import lombok.Getter;

/**
 * 营销触达类型枚举
 */
@Getter
public enum MarketingTouchTypeEnum {
    
    INBOX(1, "站内信"),
    SMS(2, "短信推送"),
    EMAIL(3, "邮件推送"),
    PUSH(4, "APP推送");
    
    private final Integer type;
    private final String name;
    
    MarketingTouchTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
}