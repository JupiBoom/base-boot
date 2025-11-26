package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.MemberExclusiveActivity;

import java.util.List;
import java.util.Map;

/**
 * 会员专享活动服务接口
 */
public interface MemberExclusiveActivityService {
    /**
     * 根据ID查询活动
     * @param id 活动ID
     * @return 活动信息
     */
    MemberExclusiveActivity findById(Long id);

    /**
     * 分页查询活动
     * @param params 查询参数
     * @return 活动列表
     */
    List<MemberExclusiveActivity> findByPage(Map<String, Object> params);

    /**
     * 保存活动
     * @param activity 活动信息
     * @return 保存后的活动信息
     */
    MemberExclusiveActivity save(MemberExclusiveActivity activity);

    /**
     * 更新活动
     * @param activity 活动信息
     * @return 更新后的活动信息
     */
    MemberExclusiveActivity update(MemberExclusiveActivity activity);

    /**
     * 删除活动
     * @param id 活动ID
     */
    void delete(Long id);

    /**
     * 审核活动
     * @param id 活动ID
     * @param status 审核状态
     * @param remark 审核备注
     */
    void audit(Long id, Integer status, String remark);

    /**
     * 查询正在进行中的活动
     * @return 活动列表
     */
    List<MemberExclusiveActivity> findCurrentActivities();

    /**
     * 查询会员可参与的活动
     * @param memberId 会员ID
     * @return 可参与活动列表
     */
    List<MemberExclusiveActivity> findAvailableActivitiesForMember(Long memberId);
}
