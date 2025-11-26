package zz.hujing.baseboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.domain.MemberExclusiveActivity;
import zz.hujing.baseboot.service.MemberExclusiveActivityService;

import java.util.List;
import java.util.Map;

/**
 * 会员专享活动管理Controller
 */
@RestController
@RequestMapping("/api/memberExclusiveActivity")
public class MemberExclusiveActivityController {

    @Autowired
    private MemberExclusiveActivityService memberExclusiveActivityService;

    /**
     * 根据ID查询会员专享活动
     */
    @GetMapping("/{id}")
    public MemberExclusiveActivity findById(@PathVariable Long id) {
        return memberExclusiveActivityService.findById(id);
    }

    /**
     * 分页查询会员专享活动
     */
    @GetMapping("/page")
    public List<MemberExclusiveActivity> findByPage(@RequestParam Map<String, Object> params) {
        return memberExclusiveActivityService.findByPage(params);
    }

    /**
     * 保存会员专享活动
     */
    @PostMapping
    public MemberExclusiveActivity save(@RequestBody MemberExclusiveActivity activity) {
        return memberExclusiveActivityService.save(activity);
    }

    /**
     * 更新会员专享活动
     */
    @PutMapping
    public MemberExclusiveActivity update(@RequestBody MemberExclusiveActivity activity) {
        return memberExclusiveActivityService.update(activity);
    }

    /**
     * 删除会员专享活动
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        memberExclusiveActivityService.delete(id);
    }

    /**
     * 审核会员专享活动
     */
    @PutMapping("/audit/{id}")
    public void audit(@PathVariable Long id, @RequestParam Integer status, @RequestParam(required = false) String remark) {
        memberExclusiveActivityService.audit(id, status, remark);
    }

    /**
     * 查询正在进行中的活动
     */
    @GetMapping("/active")
    public List<MemberExclusiveActivity> findActiveActivities() {
        return memberExclusiveActivityService.findCurrentActivities();
    }

    /**
     * 查询会员可参与的活动
     */
    @GetMapping("/availableForMember/{memberId}")
    public List<MemberExclusiveActivity> findAvailableActivitiesForMember(@PathVariable Long memberId) {
        return memberExclusiveActivityService.findAvailableActivitiesForMember(memberId);
    }
}
