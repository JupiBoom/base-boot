package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.MarketingActivity;

import java.util.List;

/**
 * 营销活动服务接口
 */
public interface MarketingActivityService {
    
    /**
     * 根据ID查询营销活动
     * @param id 营销活动ID
     * @return 营销活动信息
     */
    MarketingActivity findById(Long id);
    
    /**
     * 保存营销活动
     * @param activity 营销活动信息
     * @return 保存后的营销活动信息
     */
    MarketingActivity save(MarketingActivity activity);
    
    /**
     * 审核营销活动
     * @param id 营销活动ID
     * @param status 审核状态（2：通过，4：拒绝）
     */
    void auditActivity(Long id, Integer status);
    
    /**
     * 查询正在进行中的营销活动列表
     * @return 正在进行中的营销活动列表
     */
    List<MarketingActivity> findActiveActivities();
    
    /**
     * 查询会员可参与的营销活动列表
     * @param memberId 会员ID
     * @return 会员可参与的营销活动列表
     */
    List<MarketingActivity> findAvailableActivitiesByMember(Long memberId);
}