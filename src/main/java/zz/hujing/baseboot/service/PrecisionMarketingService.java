package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.UserGroup;
import zz.hujing.baseboot.domain.UserGroupMember;
import zz.hujing.baseboot.domain.MarketingTouchRecord;

import java.util.List;

/**
 * 精准营销服务接口
 */
public interface PrecisionMarketingService {
    
    /**
     * 根据ID查询用户分群
     * @param id 用户分群ID
     * @return 用户分群信息
     */
    UserGroup findUserGroupById(Long id);
    
    /**
     * 保存用户分群
     * @param userGroup 用户分群信息
     * @return 保存后的用户分群信息
     */
    UserGroup saveUserGroup(UserGroup userGroup);
    
    /**
     * 根据用户分群ID查询成员列表
     * @param userGroupId 用户分群ID
     * @return 成员列表
     */
    List<UserGroupMember> findUserGroupMembers(Long userGroupId);
    
    /**
     * 根据会员ID查询所属分群列表
     * @param memberId 会员ID
     * @return 分群列表
     */
    List<UserGroupMember> findUserGroupsByMember(Long memberId);
    
    /**
     * 执行用户分群（根据分群规则计算分群成员）
     * @param userGroupId 用户分群ID
     */
    void executeUserGroup(Long userGroupId);
    
    /**
     * 根据会员ID获取个性化推荐商品列表
     * @param memberId 会员ID
     * @param limit 推荐数量
     * @return 商品ID列表
     */
    List<Long> getPersonalizedRecommendations(Long memberId, Integer limit);
    
    /**
     * 发送营销触达
     * @param memberId 会员ID
     * @param type 触达类型
     * @param title 触达标题
     * @param content 触达内容
     * @return 触达记录
     */
    MarketingTouchRecord sendMarketingTouch(Long memberId, Integer type, String title, String content);
    
    /**
     * 批量发送营销触达
     * @param userGroupId 用户分群ID
     * @param type 触达类型
     * @param title 触达标题
     * @param content 触达内容
     * @return 触达记录列表
     */
    List<MarketingTouchRecord> batchSendMarketingTouch(Long userGroupId, Integer type, String title, String content);
    
    /**
     * 根据会员ID查询触达记录列表
     * @param memberId 会员ID
     * @return 触达记录列表
     */
    List<MarketingTouchRecord> findMarketingTouchRecords(Long memberId);
}