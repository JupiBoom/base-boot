package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.Member;
import zz.hujing.baseboot.domain.MemberGrowthRecord;
import zz.hujing.baseboot.domain.MemberLevelChangeRecord;

import java.util.List;
import java.util.Map;

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
     * 根据会员编号查询会员
     * @param memberNo 会员编号
     * @return 会员信息
     */
    Member findByMemberNo(String memberNo);

    /**
     * 根据手机号查询会员
     * @param phone 手机号
     * @return 会员信息
     */
    Member findByPhone(String phone);

    /**
     * 分页查询会员
     * @param params 查询参数
     * @return 会员列表
     */
    List<Member> findByPage(Map<String, Object> params);

    /**
     * 保存会员
     * @param member 会员信息
     * @return 保存后的会员信息
     */
    Member save(Member member);

    /**
     * 更新会员
     * @param member 会员信息
     * @return 更新后的会员信息
     */
    Member update(Member member);

    /**
     * 删除会员
     * @param id 会员ID
     */
    void delete(Long id);

    /**
     * 冻结会员
     * @param id 会员ID
     */
    void freeze(Long id);

    /**
     * 解冻会员
     * @param id 会员ID
     */
    void unfreeze(Long id);

    /**
     * 计算会员成长值
     * @param memberId 会员ID
     * @param consumeAmount 消费金额
     * @param activity 活跃度
     */
    void calculateGrowthValue(Long memberId, Double consumeAmount, Integer activity);

    /**
     * 更新会员等级
     * @param memberId 会员ID
     */
    void updateMemberLevel(Long memberId);

    /**
     * 查询会员成长值记录
     * @param memberId 会员ID
     * @return 成长值记录列表
     */
    List<MemberGrowthRecord> findGrowthRecords(Long memberId);

    /**
     * 查询会员等级变更记录
     * @param memberId 会员ID
     * @return 等级变更记录列表
     */
    List<MemberLevelChangeRecord> findLevelChangeRecords(Long memberId);

    /**
     * 根据会员等级查询会员
     * @param levelId 会员等级ID
     * @return 会员列表
     */
    List<Member> findByLevelId(Long levelId);

    /**
     * 用户分群（RFM模型）
     * @return 分群结果
     */
    Map<String, List<Member>> rfmSegmentation();
}
