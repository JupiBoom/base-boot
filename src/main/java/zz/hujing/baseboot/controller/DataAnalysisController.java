package zz.hujing.baseboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.service.DataAnalysisService;
import zz.hujing.baseboot.core.result.CommonResult;

import java.time.LocalDate;
import java.util.Map;

/**
 * 数据分析控制器
 */
@RestController
@RequestMapping("/api/data-analysis")
@RequiredArgsConstructor
public class DataAnalysisController {
    
    private final DataAnalysisService dataAnalysisService;
    
    /**
     * 获取会员统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    @GetMapping("/member-statistics")
    public CommonResult<Map<String, Object>> getMemberStatistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> statistics = dataAnalysisService.getMemberStatistics(startDate, endDate);
        return CommonResult.success(statistics);
    }
    
    /**
     * 获取会员增长趋势
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 增长趋势数据
     */
    @GetMapping("/member-growth-trend")
    public CommonResult<Map<String, Object>> getMemberGrowthTrend(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> trend = dataAnalysisService.getMemberGrowthTrend(startDate, endDate);
        return CommonResult.success(trend);
    }
    
    /**
     * 获取会员等级分布
     * @return 等级分布数据
     */
    @GetMapping("/member-level-distribution")
    public CommonResult<Map<String, Object>> getMemberLevelDistribution() {
        Map<String, Object> distribution = dataAnalysisService.getMemberLevelDistribution();
        return CommonResult.success(distribution);
    }
    
    /**
     * 获取优惠券统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    @GetMapping("/coupon-statistics")
    public CommonResult<Map<String, Object>> getCouponStatistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> statistics = dataAnalysisService.getCouponStatistics(startDate, endDate);
        return CommonResult.success(statistics);
    }
    
    /**
     * 获取优惠券使用趋势
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 使用趋势数据
     */
    @GetMapping("/coupon-usage-trend")
    public CommonResult<Map<String, Object>> getCouponUsageTrend(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> trend = dataAnalysisService.getCouponUsageTrend(startDate, endDate);
        return CommonResult.success(trend);
    }
    
    /**
     * 获取营销活动统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据
     */
    @GetMapping("/activity-statistics")
    public CommonResult<Map<String, Object>> getActivityStatistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> statistics = dataAnalysisService.getActivityStatistics(startDate, endDate);
        return CommonResult.success(statistics);
    }
    
    /**
     * 获取营销活动效果分析
     * @param activityId 活动ID
     * @return 效果分析数据
     */
    @GetMapping("/activity-effect/{activityId}")
    public CommonResult<Map<String, Object>> getActivityEffectAnalysis(@PathVariable Long activityId) {
        Map<String, Object> analysis = dataAnalysisService.getActivityEffectAnalysis(activityId);
        return CommonResult.success(analysis);
    }
}