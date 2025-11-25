package zz.hujing.baseboot.domain.enums;

import lombok.Getter;

/**
 * 用户分群类型枚举
 */
@Getter
public enum UserGroupTypeEnum {
    
    RFM(1, "RFM模型分群"),
    BEHAVIOR(2, "行为分群"),
    DEMOGRAPHIC(3, "人口统计学分群"),
    PREFERENCE(4, "偏好分群");
    
    private final Integer type;
    private final String name;
    
    UserGroupTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
}