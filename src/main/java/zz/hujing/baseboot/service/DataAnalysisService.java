package zz.hujing.baseboot.service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 数据分析服务接口
 */
public interface DataAnalysisService {
    /**
     * 计算会员留存率
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 留存率数据
     */
    Map<String, Object> calculateMemberRetentionRate(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 计算营销活动ROI
     * @param activityId 活动ID
     * @param activityType 活动类型（0：优惠券，1：限时折扣，2：会员专享）
     * @return ROI数据
     */
    Map<String, Object> calculateActivityROI(Long activityId, Integer activityType);

    /**
     * 统计会员增长趋势
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 增长趋势数据
     */
    Map<String, Object>统计MemberGrowthTrend(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计优惠券使用情况
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 优惠券使用数据
     */
    Map<String, Object>统计CouponUsage(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计限时折扣活动效果
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 活动效果数据
     */
    Map<String, Object>统计FlashSaleActivityEffect(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计会员等级分布
     * @return 等级分布数据
     */
    Map<String, Object>统计MemberLevelDistribution();
}
