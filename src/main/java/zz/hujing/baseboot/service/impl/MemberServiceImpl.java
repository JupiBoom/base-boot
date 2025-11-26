package zz.hujing.baseboot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zz.hujing.baseboot.domain.Member;
import zz.hujing.baseboot.domain.MemberGrowthRecord;
import zz.hujing.baseboot.domain.MemberLevel;
import zz.hujing.baseboot.domain.MemberLevelChangeRecord;
import zz.hujing.baseboot.repository.MemberRepository;
import zz.hujing.baseboot.repository.MemberGrowthRecordRepository;
import zz.hujing.baseboot.repository.MemberLevelRepository;
import zz.hujing.baseboot.repository.MemberLevelChangeRecordRepository;
import zz.hujing.baseboot.service.MemberService;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 会员服务实现类
 */
@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberGrowthRecordRepository growthRecordRepository;

    @Autowired
    private MemberLevelRepository levelRepository;

    @Autowired
    private MemberLevelChangeRecordRepository levelChangeRecordRepository;

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @Override
    public Member findByMemberNo(String memberNo) {
        return memberRepository.findByMemberNo(memberNo);
    }

    @Override
    public Member findByPhone(String phone) {
        return memberRepository.findByPhone(phone);
    }

    @Override
    public List<Member> findByPage(Map<String, Object> params) {
        int page = params.getOrDefault("page", 1) != null ? (int) params.get("page") : 1;
        int size = params.getOrDefault("size", 10) != null ? (int) params.get("size") : 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "registerTime"));

        Specification<Member> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 根据会员编号查询
            if (params.containsKey("memberNo") && params.get("memberNo") != null) {
                predicates.add(criteriaBuilder.like(root.get("memberNo"), "%" + params.get("memberNo") + "%"));
            }
            // 根据手机号查询
            if (params.containsKey("phone") && params.get("phone") != null) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + params.get("phone") + "%"));
            }
            // 根据会员等级查询
            if (params.containsKey("levelId") && params.get("levelId") != null) {
                predicates.add(criteriaBuilder.equal(root.get("levelId"), params.get("levelId")));
            }
            // 根据状态查询
            if (params.containsKey("status") && params.get("status") != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), params.get("status")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Member> memberPage = memberRepository.findAll(specification, pageable);
        return memberPage.getContent();
    }

    @Override
    @Transactional
    public Member save(Member member) {
        // 生成会员编号
        if (member.getMemberNo() == null) {
            member.setMemberNo(generateMemberNo());
        }
        // 默认等级为普通会员
        if (member.getLevelId() == null) {
            MemberLevel commonLevel = levelRepository.findByCode("common");
            if (commonLevel != null) {
                member.setLevelId(commonLevel.getId());
            }
        }
        // 默认成长值为0
        if (member.getGrowthValue() == null) {
            member.setGrowthValue(0);
        }
        // 默认总消费金额为0
        if (member.getTotalConsume() == null) {
            member.setTotalConsume(BigDecimal.ZERO);
        }
        // 默认活跃度为0
        if (member.getActivity() == null) {
            member.setActivity(0);
        }
        // 默认状态为正常
        if (member.getStatus() == null) {
            member.setStatus(0);
        }
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member update(Member member) {
        Member existingMember = memberRepository.findById(member.getId()).orElse(null);
        if (existingMember == null) {
            throw new RuntimeException("会员不存在");
        }
        // 更新会员信息
        existingMember.setName(member.getName());
        existingMember.setEmail(member.getEmail());
        existingMember.setPreferenceTags(member.getPreferenceTags());
        existingMember.setLastLoginTime(member.getLastLoginTime());
        return memberRepository.save(existingMember);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void freeze(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        if (member == null) {
            throw new RuntimeException("会员不存在");
        }
        member.setStatus(1);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void unfreeze(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        if (member == null) {
            throw new RuntimeException("会员不存在");
        }
        member.setStatus(0);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void calculateGrowthValue(Long memberId, Double consumeAmount, Integer activity) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            throw new RuntimeException("会员不存在");
        }

        // 消费金额转换为成长值（1元=1成长值）
        int consumeGrowth = consumeAmount != null ? (int) Math.round(consumeAmount) : 0;
        // 活跃度转换为成长值（1活跃度=1成长值）
        int activityGrowth = activity != null ? activity : 0;
        // 总成长值
        int totalGrowth = consumeGrowth + activityGrowth;

        if (totalGrowth > 0) {
            // 更新会员成长值和总消费金额
            int newGrowthValue = member.getGrowthValue() + totalGrowth;
            member.setGrowthValue(newGrowthValue);
            if (consumeAmount != null) {
                member.setTotalConsume(member.getTotalConsume().add(BigDecimal.valueOf(consumeAmount)));
            }
            if (activity != null) {
                member.setActivity(member.getActivity() + activity);
            }
            memberRepository.save(member);

            // 保存成长值记录
            MemberGrowthRecord growthRecord = new MemberGrowthRecord();
            growthRecord.setMemberId(memberId);
            growthRecord.setChangeType(consumeGrowth > 0 ? 0 : 1);
            growthRecord.setChangeValue(totalGrowth);
            growthRecord.setAfterGrowthValue(newGrowthValue);
            growthRecord.setRemark(consumeGrowth > 0 ? "消费增加成长值" : "活跃度增加成长值");
            growthRecordRepository.save(growthRecord);

            // 更新会员等级
            updateMemberLevel(memberId);
        }
    }

    @Override
    @Transactional
    public void updateMemberLevel(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            throw new RuntimeException("会员不存在");
        }

        // 查询所有启用的会员等级
        List<MemberLevel> levels = levelRepository.findByStatusOrderByMinGrowthValueAsc(0);
        if (levels.isEmpty()) {
            throw new RuntimeException("会员等级配置为空");
        }

        // 确定当前会员等级
        MemberLevel currentLevel = null;
        for (MemberLevel level : levels) {
            if (member.getGrowthValue() >= level.getMinGrowthValue() && 
                (level.getMaxGrowthValue() == null || member.getGrowthValue() < level.getMaxGrowthValue())) {
                currentLevel = level;
            }
        }
        // 如果没有匹配的等级，取最高等级
        if (currentLevel == null) {
            currentLevel = levels.get(levels.size() - 1);
        }

        // 如果等级发生变化，记录变更历史
        if (!currentLevel.getId().equals(member.getLevelId())) {
            MemberLevel oldLevel = levelRepository.findById(member.getLevelId()).orElse(null);
            member.setLevelId(currentLevel.getId());
            memberRepository.save(member);

            // 保存等级变更记录
            MemberLevelChangeRecord changeRecord = new MemberLevelChangeRecord();
            changeRecord.setMemberId(memberId);
            changeRecord.setOldLevelId(member.getLevelId());
            changeRecord.setNewLevelId(currentLevel.getId());
            changeRecord.setChangeReason("成长值变化导致等级变更");
            levelChangeRecordRepository.save(changeRecord);

            log.info("会员{}等级变更：从{}到{}", member.getMemberNo(), 
                    oldLevel != null ? oldLevel.getName() : "未知", currentLevel.getName());
        }
    }

    @Override
    public List<MemberGrowthRecord> findGrowthRecords(Long memberId) {
        return growthRecordRepository.findByMemberIdOrderByCreateTimeDesc(memberId);
    }

    @Override
    public List<MemberLevelChangeRecord> findLevelChangeRecords(Long memberId) {
        return levelChangeRecordRepository.findByMemberIdOrderByCreateTimeDesc(memberId);
    }

    @Override
    public List<Member> findByLevelId(Long levelId) {
        Specification<Member> specification = (root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("levelId"), levelId);
        return memberRepository.findAll(specification);
    }

    @Override
    public Map<String, List<Member>> rfmSegmentation() {
        // TODO: 实现RFM模型用户分群
        return null;
    }

    /**
     * 生成会员编号
     * @return 会员编号
     */
    private String generateMemberNo() {
        // 会员编号格式：M + 年份后两位 + 月份 + 日 + 6位流水号
        LocalDateTime now = LocalDateTime.now();
        String datePart = String.format("%02d%02d%02d", now.getYear() % 100, now.getMonthValue(), now.getDayOfMonth());
        // 生成6位流水号
        String serialPart = String.format("%06d", (int) (Math.random() * 1000000));
        return "M" + datePart + serialPart;
    }
}
