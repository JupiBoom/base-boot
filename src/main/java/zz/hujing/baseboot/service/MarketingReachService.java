package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.MarketingReachRecord;

import java.util.List;
import java.util.Map;

/**
 * 营销触达服务接口
 */
public interface MarketingReachService {
    /**
     * 根据ID查询触达记录
     * @param id 触达记录ID
     * @return 触达记录信息
     */
    MarketingReachRecord findById(Long id);

    /**
     * 分页查询触达记录
     * @param params 查询参数
     * @return 触达记录列表
     */
    List<MarketingReachRecord> findByPage(Map<String, Object> params);

    /**
     * 保存触达记录
     * @param record 触达记录信息
     * @return 保存后的触达记录信息
     */
    MarketingReachRecord save(MarketingReachRecord record);

    /**
     * 更新触达记录
     * @param record 触达记录信息
     * @return 更新后的触达记录信息
     */
    MarketingReachRecord update(MarketingReachRecord record);

    /**
     * 删除触达记录
     * @param id 触达记录ID
     */
    void delete(Long id);

    /**
     * 发送站内信
     * @param memberId 会员ID
     * @param content 内容
     * @return 触达记录信息
     */
    MarketingReachRecord sendInnerMessage(Long memberId, String content);

    /**
     * 发送短信推送
     * @param memberId 会员ID
     * @param content 内容
     * @return 触达记录信息
     */
    MarketingReachRecord sendSmsMessage(Long memberId, String content);

    /**
     * 批量发送营销消息
     * @param memberIds 会员ID列表
     * @param content 内容
     * @param reachType 触达类型
     * @return 发送结果
     */
    Map<String, Object> batchSendMessage(List<Long> memberIds, String content, Integer reachType);

    /**
     * 查询会员触达记录
     * @param memberId 会员ID
     * @return 触达记录列表
     */
    List<MarketingReachRecord> findByMemberId(Long memberId);

    /**
     * 查询待发送的触达记录
     * @return 触达记录列表
     */
    List<MarketingReachRecord> findPendingRecords();

    /**
     * 重新发送触达记录
     * @param id 触达记录ID
     * @return 触达记录信息
     */
    MarketingReachRecord resendRecord(Long id);
}
