package zz.hujing.baseboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.domain.MarketingReachRecord;
import zz.hujing.baseboot.service.MarketingReachService;

import java.util.List;
import java.util.Map;

/**
 * 营销触达管理Controller
 */
@RestController
@RequestMapping("/api/marketingReach")
public class MarketingReachController {

    @Autowired
    private MarketingReachService marketingReachService;

    /**
     * 根据ID查询营销触达记录
     */
    @GetMapping("/{id}")
    public MarketingReachRecord findById(@PathVariable Long id) {
        return marketingReachService.findById(id);
    }

    /**
     * 分页查询营销触达记录
     */
    @GetMapping("/page")
    public List<MarketingReachRecord> findByPage(@RequestParam Map<String, Object> params) {
        return marketingReachService.findByPage(params);
    }

    /**
     * 保存营销触达记录
     */
    @PostMapping
    public MarketingReachRecord save(@RequestBody MarketingReachRecord record) {
        return marketingReachService.save(record);
    }

    /**
     * 更新营销触达记录
     */
    @PutMapping
    public MarketingReachRecord update(@RequestBody MarketingReachRecord record) {
        return marketingReachService.update(record);
    }

    /**
     * 删除营销触达记录
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        marketingReachService.delete(id);
    }

    /**
     * 根据会员ID查询营销触达记录
     */
    @GetMapping("/byMemberId/{memberId}")
    public List<MarketingReachRecord> findByMemberId(@PathVariable Long memberId) {
        return marketingReachService.findByMemberId(memberId);
    }

    /**
     * 查询待发送的营销触达记录
     */
    @GetMapping("/pending")
    public List<MarketingReachRecord> findPendingRecords() {
        return marketingReachService.findPendingRecords();
    }

    /**
     * 发送站内信
     */
    @PostMapping("/sendInternalMessage/{memberId}")
    public void sendInternalMessage(@PathVariable Long memberId, @RequestParam String content) {
        marketingReachService.sendInternalMessage(memberId, content);
    }

    /**
     * 发送短信
     */
    @PostMapping("/sendSms/{memberId}")
    public void sendSms(@PathVariable Long memberId, @RequestParam String phoneNumber, @RequestParam String content) {
        marketingReachService.sendSms(memberId, phoneNumber, content);
    }

    /**
     * 批量发送营销消息
     */
    @PostMapping("/sendMarketingMessage")
    public void sendMarketingMessage(@RequestParam List<Long> memberIds, @RequestParam Integer reachType, @RequestParam String content) {
        marketingReachService.sendMarketingMessage(memberIds, reachType, content);
    }

    /**
     * 重新发送营销触达记录
     */
    @PutMapping("/resend/{id}")
    public void resendRecord(@PathVariable Long id) {
        marketingReachService.resendRecord(id);
    }
}
