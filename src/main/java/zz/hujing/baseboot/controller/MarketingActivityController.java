package zz.hujing.baseboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.core.result.CommonResult;
import zz.hujing.baseboot.domain.MarketingActivity;
import zz.hujing.baseboot.service.MarketingActivityService;

import java.util.List;

/**
 * 营销活动控制器
 */
@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class MarketingActivityController {
    
    private final MarketingActivityService marketingActivityService;
    
    /**
     * 根据ID查询营销活动
     * @param id 营销活动ID
     * @return 营销活动信息
     */
    @GetMapping("/{id}")
    public CommonResult<MarketingActivity> findById(@PathVariable Long id) {
        MarketingActivity activity = marketingActivityService.findById(id);
        return CommonResult.success(activity);
    }
    
    /**
     * 保存营销活动
     * @param activity 营销活动信息
     * @return 保存后的营销活动信息
     */
    @PostMapping
    public CommonResult<MarketingActivity> save(@RequestBody MarketingActivity activity) {
        MarketingActivity savedActivity = marketingActivityService.save(activity);
        return CommonResult.success(savedActivity);
    }
    
    /**
     * 审核营销活动
     * @param id 营销活动ID
     * @param status 审核状态
     * @return 操作结果
     */
    @PutMapping("/audit")
    public CommonResult<Void> auditActivity(@RequestParam Long id, @RequestParam Integer status) {
        marketingActivityService.auditActivity(id, status);
        return CommonResult.success();
    }
    
    /**
     * 查询正在进行中的营销活动列表
     * @return 正在进行中的营销活动列表
     */
    @GetMapping("/active")
    public CommonResult<List<MarketingActivity>> findActiveActivities() {
        List<MarketingActivity> activityList = marketingActivityService.findActiveActivities();
        return CommonResult.success(activityList);
    }
    
    /**
     * 查询会员可参与的营销活动列表
     * @param memberId 会员ID
     * @return 会员可参与的营销活动列表
     */
    @GetMapping("/available/{memberId}")
    public CommonResult<List<MarketingActivity>> findAvailableActivitiesByMember(@PathVariable Long memberId) {
        List<MarketingActivity> activityList = marketingActivityService.findAvailableActivitiesByMember(memberId);
        return CommonResult.success(activityList);
    }
}