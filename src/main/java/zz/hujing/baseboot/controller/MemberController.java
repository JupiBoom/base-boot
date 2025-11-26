package zz.hujing.baseboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.domain.Member;
import zz.hujing.baseboot.domain.MemberGrowthRecord;
import zz.hujing.baseboot.domain.MemberLevelChangeRecord;
import zz.hujing.baseboot.service.MemberService;

import java.util.List;
import java.util.Map;

/**
 * 会员管理Controller
 */
@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 根据ID查询会员
     */
    @GetMapping("/{id}")
    public Member findById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    /**
     * 根据会员编号查询会员
     */
    @GetMapping("/byMemberNo/{memberNo}")
    public Member findByMemberNo(@PathVariable String memberNo) {
        return memberService.findByMemberNo(memberNo);
    }

    /**
     * 根据手机号查询会员
     */
    @GetMapping("/byPhone/{phone}")
    public Member findByPhone(@PathVariable String phone) {
        return memberService.findByPhone(phone);
    }

    /**
     * 分页查询会员
     */
    @GetMapping("/page")
    public List<Member> findByPage(@RequestParam Map<String, Object> params) {
        return memberService.findByPage(params);
    }

    /**
     * 查询所有会员
     */
    @GetMapping("/all")
    public List<Member> findAll() {
        // 使用findByPage方法查询所有会员，不设置分页参数
        return memberService.findByPage(null);
    }

    /**
     * 根据等级查询会员
     */
    @GetMapping("/byLevel/{levelId}")
    public List<Member> findByLevelId(@PathVariable Long levelId) {
        return memberService.findByLevelId(levelId);
    }

    /**
     * 保存会员
     */
    @PostMapping
    public Member save(@RequestBody Member member) {
        return memberService.save(member);
    }

    /**
     * 更新会员
     */
    @PutMapping
    public Member update(@RequestBody Member member) {
        return memberService.update(member);
    }

    /**
     * 删除会员
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        memberService.delete(id);
    }

    /**
     * 冻结会员
     */
    @PutMapping("/freeze/{id}")
    public void freeze(@PathVariable Long id) {
        memberService.freeze(id);
    }

    /**
     * 解冻会员
     */
    @PutMapping("/unfreeze/{id}")
    public void unfreeze(@PathVariable Long id) {
        memberService.unfreeze(id);
    }

    /**
     * 计算会员成长值
     */
    @PostMapping("/calculateGrowth/{memberId}")
    public void calculateGrowth(@PathVariable Long memberId, @RequestParam Double amount, @RequestParam Integer activity) {
        memberService.calculateGrowthValue(memberId, amount, activity);
    }

    /**
     * 更新会员等级
     */
    @PutMapping("/updateLevel/{memberId}")
    public void updateMemberLevel(@PathVariable Long memberId) {
        memberService.updateMemberLevel(memberId);
    }

    /**
     * 查询会员成长值记录
     */
    @GetMapping("/growthRecords/{memberId}")
    public List<MemberGrowthRecord> findGrowthRecords(@PathVariable Long memberId) {
        return memberService.findGrowthRecords(memberId);
    }

    /**
     * 查询会员等级变更记录
     */
    @GetMapping("/levelChangeRecords/{memberId}")
    public List<MemberLevelChangeRecord> findLevelChangeRecords(@PathVariable Long memberId) {
        return memberService.findLevelChangeRecords(memberId);
    }

    /**
     * RFM用户分群
     */
    @GetMapping("/rfmSegmentation")
    public Map<String, List<Member>> rfmSegmentation() {
        return memberService.rfmSegmentation();
    }
}
