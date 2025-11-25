package zz.hujing.baseboot.service;

import java.time.LocalDate;
import java.util.Map;

/**
 * 数据分析服务接口
 */
public interface DataAnalysisService {
    
    /**
     * 获取会员统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    Map<String, Object> getMemberStatistics(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取会员增长趋势
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 增长趋势数据
     */
    Map<String, Object> getMemberGrowthTrend(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取会员等级分布
     * @return 等级分布数据
     */
    Map<String, Object> getMemberLevelDistribution();
    
    /**
     * 获取优惠券统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    Map<String, Object> getCouponStatistics(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取优惠券使用趋势
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 使用趋势数据
     */
    Map<String, Object> getCouponUsageTrend(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取营销活动统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    Map<String, Object> getActivityStatistics(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取营销活动效果分析
     * @param activityId 活动ID
     * @return 效果分析数据
     */
    Map<String, Object> getActivityEffectAnalysis(Long activityId);
}