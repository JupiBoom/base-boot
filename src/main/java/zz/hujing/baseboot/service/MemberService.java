package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.Member;
import zz.hujing.baseboot.domain.MemberLevelHistory;

import java.util.List;

/**
 * 会员服务接口
 */
public interface MemberService {
    
    /**
     * 根据ID查询会员
     * @param id 会员ID
     * @return 会员信息
     */
    Member findById(Long id);
    
    /**
     * 根据手机号码查询会员
     * @param phone 手机号码
     * @return 会员信息
     */
    Member findByPhone(String phone);
    
    /**
     * 保存会员
     * @param member 会员信息
     * @return 保存后的会员信息
     */
    Member save(Member member);
    
    /**
     * 更新会员信息
     * @param member 会员信息
     * @return 更新后的会员信息
     */
    Member update(Member member);
    
    /**
     * 更新会员成长值
     * @param memberId 会员ID
     * @param growthValue 成长值变化量（正数为增加，负数为减少）
     */
    void updateGrowthValue(Long memberId, Integer growthValue);
    
    /**
     * 查询会员等级历史记录
     * @param memberId 会员ID
     * @return 会员等级历史记录列表
     */
    List<MemberLevelHistory> findMemberLevelHistory(Long memberId);
    
    /**
     * 根据会员等级查询会员列表
     * @param level 会员等级
     * @return 会员列表
     */
    List<Member> findByLevel(Integer level);
}