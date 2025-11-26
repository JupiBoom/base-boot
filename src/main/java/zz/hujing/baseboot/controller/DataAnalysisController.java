package zz.hujing.baseboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zz.hujing.baseboot.service.DataAnalysisService;

import java.util.List;
import java.util.Map;

/**
 * 数据分析Controller
 */
@RestController
@RequestMapping("/api/dataAnalysis")
public class DataAnalysisController {

    @Autowired
    private DataAnalysisService dataAnalysisService;

    /**
     * 分析会员留存率
     */
    @GetMapping("/memberRetention")
    public Map<String, Object> analyzeMemberRetention() {
        return dataAnalysisService.analyzeMemberRetention();
    }

    /**
     * 计算营销活动ROI
     */
    @GetMapping("/marketingROI")
    public Map<String, Object> calculateMarketingROI() {
        return dataAnalysisService.calculateMarketingROI();
    }

    /**
     * 分析会员增长趋势
     */
    @GetMapping("/memberGrowthTrend")
    public Map<String, Object> analyzeMemberGrowthTrend() {
        return dataAnalysisService.analyzeMemberGrowthTrend();
    }

    /**
     * 分析优惠券使用情况
     */
    @GetMapping("/couponUsage")
    public Map<String, Object> analyzeCouponUsage() {
        return dataAnalysisService.analyzeCouponUsage();
    }

    /**
     * 分析限时折扣活动效果
     */
    @GetMapping("/flashSaleEffect")
    public Map<String, Object> analyzeFlashSaleEffect() {
        return dataAnalysisService.analyzeFlashSaleEffect();
    }

    /**
     * 分析会员等级分布
     */
    @GetMapping("/memberLevelDistribution")
    public Map<String, Object> analyzeMemberLevelDistribution() {
        return dataAnalysisService.analyzeMemberLevelDistribution();
    }
}
