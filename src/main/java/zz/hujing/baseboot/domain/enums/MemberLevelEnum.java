package zz.hujing.baseboot.domain.enums;

import lombok.Getter;

/**
 * 会员等级枚举
 */
@Getter
public enum MemberLevelEnum {
    
    NORMAL(1, "普通会员", 0),
    SILVER(2, "白银会员", 1000),
    GOLD(3, "黄金会员", 5000),
    DIAMOND(4, "钻石会员", 20000);
    
    private final Integer level;
    private final String name;
    private final Integer minGrowthValue;
    
    MemberLevelEnum(Integer level, String name, Integer minGrowthValue) {
        this.level = level;
        this.name = name;
        this.minGrowthValue = minGrowthValue;
    }
    
    /**
     * 根据成长值获取会员等级
     * @param growthValue 成长值
     * @return 会员等级
     */
    public static MemberLevelEnum getByGrowthValue(Integer growthValue) {
        if (growthValue >= DIAMOND.getMinGrowthValue()) {
            return DIAMOND;
        } else if (growthValue >= GOLD.getMinGrowthValue()) {
            return GOLD;
        } else if (growthValue >= SILVER.getMinGrowthValue()) {
            return SILVER;
        } else {
            return NORMAL;
        }
    }
}