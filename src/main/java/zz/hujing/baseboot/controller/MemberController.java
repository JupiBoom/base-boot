package zz.hujing.baseboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.core.result.CommonResult;
import zz.hujing.baseboot.domain.Member;
import zz.hujing.baseboot.domain.MemberLevelHistory;
import zz.hujing.baseboot.service.MemberService;

import java.util.List;

/**
 * 会员控制器
 */
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    /**
     * 根据ID查询会员
     * @param id 会员ID
     * @return 会员信息
     */
    @GetMapping("/{id}")
    public CommonResult<Member> findById(@PathVariable Long id) {
        Member member = memberService.findById(id);
        return CommonResult.success(member);
    }
    
    /**
     * 根据手机号码查询会员
     * @param phone 手机号码
     * @return 会员信息
     */
    @GetMapping("/by-phone")
    public CommonResult<Member> findByPhone(@RequestParam String phone) {
        Member member = memberService.findByPhone(phone);
        return CommonResult.success(member);
    }
    
    /**
     * 保存会员
     * @param member 会员信息
     * @return 保存后的会员信息
     */
    @PostMapping
    public CommonResult<Member> save(@RequestBody Member member) {
        Member savedMember = memberService.save(member);
        return CommonResult.success(savedMember);
    }
    
    /**
     * 更新会员信息
     * @param member 会员信息
     * @return 更新后的会员信息
     */
    @PutMapping
    public CommonResult<Member> update(@RequestBody Member member) {
        Member updatedMember = memberService.update(member);
        return CommonResult.success(updatedMember);
    }
    
    /**
     * 更新会员成长值
     * @param memberId 会员ID
     * @param growthValue 成长值变化量
     * @return 操作结果
     */
    @PutMapping("/growth-value")
    public CommonResult<Void> updateGrowthValue(@RequestParam Long memberId, @RequestParam Integer growthValue) {
        memberService.updateGrowthValue(memberId, growthValue);
        return CommonResult.success();
    }
    
    /**
     * 查询会员等级历史记录
     * @param memberId 会员ID
     * @return 会员等级历史记录列表
     */
    @GetMapping("/level-history/{memberId}")
    public CommonResult<List<MemberLevelHistory>> findMemberLevelHistory(@PathVariable Long memberId) {
        List<MemberLevelHistory> historyList = memberService.findMemberLevelHistory(memberId);
        return CommonResult.success(historyList);
    }
    
    /**
     * 根据会员等级查询会员列表
     * @param level 会员等级
     * @return 会员列表
     */
    @GetMapping("/by-level")
    public CommonResult<List<Member>> findByLevel(@RequestParam Integer level) {
        List<Member> memberList = memberService.findByLevel(level);
        return CommonResult.success(memberList);
    }
}