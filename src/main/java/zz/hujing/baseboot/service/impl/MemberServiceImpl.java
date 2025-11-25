package zz.hujing.baseboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zz.hujing.baseboot.domain.Member;
import zz.hujing.baseboot.domain.MemberLevelHistory;
import zz.hujing.baseboot.domain.enums.MemberLevelEnum;
import zz.hujing.baseboot.repository.MemberLevelHistoryRepository;
import zz.hujing.baseboot.repository.MemberRepository;
import zz.hujing.baseboot.service.MemberService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 会员服务实现类
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    
    private final MemberRepository memberRepository;
    private final MemberLevelHistoryRepository memberLevelHistoryRepository;
    
    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }
    
    @Override
    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone).orElse(null);
    }
    
    @Override
    public Member save(Member member) {
        // 生成会员编号
        member.setMemberNo(generateMemberNo());
        // 初始化成长值和会员等级
        member.setGrowthValue(0);
        member.setCurrentLevel(MemberLevelEnum.NORMAL);
        // 设置注册时间
        member.setRegisterTime(LocalDateTime.now());
        return memberRepository.save(member);
    }
    
    @Override
    public Member update(Member member) {
        return memberRepository.save(member);
    }
    
    @Override
    @Transactional
    public void updateGrowthValue(Long memberId, Integer growthValue) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("会员不存在"));
        
        // 更新成长值
        Integer newGrowthValue = member.getGrowthValue() + growthValue;
        member.setGrowthValue(newGrowthValue);
        
        // 计算新的会员等级
        MemberLevelEnum newLevel = MemberLevelEnum.getByGrowthValue(newGrowthValue);
        
        // 如果会员等级发生变化，记录历史记录
        if (!member.getCurrentLevel().equals(newLevel)) {
            MemberLevelHistory history = new MemberLevelHistory();
            history.setMemberId(memberId);
            history.setOldLevel(member.getCurrentLevel());
            history.setNewLevel(newLevel);
            history.setReason("成长值变化：" + growthValue);
            memberLevelHistoryRepository.save(history);
            
            // 更新会员当前等级
            member.setCurrentLevel(newLevel);
        }
        
        // 保存会员信息
        memberRepository.save(member);
    }
    
    @Override
    public List<MemberLevelHistory> findMemberLevelHistory(Long memberId) {
        return memberLevelHistoryRepository.findByMemberId(memberId);
    }
    
    @Override
    public List<Member> findByLevel(Integer level) {
        MemberLevelEnum memberLevel = MemberLevelEnum.values()[level - 1];
        return memberRepository.findByCurrentLevel(memberLevel);
    }
    
    /**
     * 生成会员编号
     * @return 会员编号
     */
    private String generateMemberNo() {
        // 简单生成规则：前缀+时间戳+随机数
        return "MEM" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}