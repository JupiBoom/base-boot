package zz.hujing.baseboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.domain.UserGroup;
import zz.hujing.baseboot.domain.UserGroupMember;
import zz.hujing.baseboot.domain.MarketingTouchRecord;
import zz.hujing.baseboot.service.PrecisionMarketingService;
import zz.hujing.baseboot.core.result.CommonResult;

import java.util.List;

/**
 * 精准营销控制器
 */
@RestController
@RequestMapping("/api/precision-marketing")
@RequiredArgsConstructor
public class PrecisionMarketingController {
    
    private final PrecisionMarketingService precisionMarketingService;
    
    /**
     * 根据ID查询用户分群
     * @param id 用户分群ID
     * @return 用户分群信息
     */
    @GetMapping("/user-group/{id}")
    public CommonResult<UserGroup> findUserGroupById(@PathVariable Long id) {
        UserGroup userGroup = precisionMarketingService.findUserGroupById(id);
        return CommonResult.success(userGroup);
    }
    
    /**
     * 保存用户分群
     * @param userGroup 用户分群信息
     * @return 保存后的用户分群信息
     */
    @PostMapping("/user-group")
    public CommonResult<UserGroup> saveUserGroup(@RequestBody UserGroup userGroup) {
        UserGroup savedUserGroup = precisionMarketingService.saveUserGroup(userGroup);
        return CommonResult.success(savedUserGroup);
    }
    
    /**
     * 根据用户分群ID查询成员列表
     * @param userGroupId 用户分群ID
     * @return 成员列表
     */
    @GetMapping("/user-group/{userGroupId}/members")
    public CommonResult<List<UserGroupMember>> findUserGroupMembers(@PathVariable Long userGroupId) {
        List<UserGroupMember> members = precisionMarketingService.findUserGroupMembers(userGroupId);
        return CommonResult.success(members);
    }
    
    /**
     * 根据会员ID查询所属分群列表
     * @param memberId 会员ID
     * @return 分群列表
     */
    @GetMapping("/user-groups/member/{memberId}")
    public CommonResult<List<UserGroupMember>> findUserGroupsByMember(@PathVariable Long memberId) {
        List<UserGroupMember> userGroups = precisionMarketingService.findUserGroupsByMember(memberId);
        return CommonResult.success(userGroups);
    }
    
    /**
     * 执行用户分群
     * @param userGroupId 用户分群ID
     * @return 执行结果
     */
    @PostMapping("/user-group/{userGroupId}/execute")
    public CommonResult<Void> executeUserGroup(@PathVariable Long userGroupId) {
        precisionMarketingService.executeUserGroup(userGroupId);
        return CommonResult.success();
    }
    
    /**
     * 根据会员ID获取个性化推荐商品列表
     * @param memberId 会员ID
     * @param limit 推荐数量
     * @return 商品ID列表
     */
    @GetMapping("/recommendations/{memberId}")
    public CommonResult<List<Long>> getPersonalizedRecommendations(@PathVariable Long memberId, @RequestParam(defaultValue = "10") Integer limit) {
        List<Long> recommendations = precisionMarketingService.getPersonalizedRecommendations(memberId, limit);
        return CommonResult.success(recommendations);
    }
    
    /**
     * 发送营销触达
     * @param memberId 会员ID
     * @param type 触达类型
     * @param title 触达标题
     * @param content 触达内容
     * @return 触达记录
     */
    @PostMapping("/touch")
    public CommonResult<MarketingTouchRecord> sendMarketingTouch(@RequestParam Long memberId, @RequestParam Integer type, @RequestParam String title, @RequestParam String content) {
        MarketingTouchRecord record = precisionMarketingService.sendMarketingTouch(memberId, type, title, content);
        return CommonResult.success(record);
    }
    
    /**
     * 批量发送营销触达
     * @param userGroupId 用户分群ID
     * @param type 触达类型
     * @param title 触达标题
     * @param content 触达内容
     * @return 触达记录列表
     */
    @PostMapping("/touch/batch")
    public CommonResult<List<MarketingTouchRecord>> batchSendMarketingTouch(@RequestParam Long userGroupId, @RequestParam Integer type, @RequestParam String title, @RequestParam String content) {
        List<MarketingTouchRecord> records = precisionMarketingService.batchSendMarketingTouch(userGroupId, type, title, content);
        return CommonResult.success(records);
    }
    
    /**
     * 根据会员ID查询触达记录列表
     * @param memberId 会员ID
     * @return 触达记录列表
     */
    @GetMapping("/touch-records/{memberId}")
    public CommonResult<List<MarketingTouchRecord>> findMarketingTouchRecords(@PathVariable Long memberId) {
        List<MarketingTouchRecord> records = precisionMarketingService.findMarketingTouchRecords(memberId);
        return CommonResult.success(records);
    }
}