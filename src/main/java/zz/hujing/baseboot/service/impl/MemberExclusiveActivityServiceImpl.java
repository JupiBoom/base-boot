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
import zz.hujing.baseboot.domain.MemberExclusiveActivity;
import zz.hujing.baseboot.repository.MemberExclusiveActivityRepository;
import zz.hujing.baseboot.service.MemberExclusiveActivityService;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 会员专享活动服务实现类
 */
@Slf4j
@Service
public class MemberExclusiveActivityServiceImpl implements MemberExclusiveActivityService {

    @Autowired
    private MemberExclusiveActivityRepository memberExclusiveActivityRepository;

    @Override
    public MemberExclusiveActivity findById(Long id) {
        return memberExclusiveActivityRepository.findById(id).orElse(null);
    }

    @Override
    public List<MemberExclusiveActivity> findByPage(Map<String, Object> params) {
        int page = params.getOrDefault("page", 1) != null ? (int) params.get("page") : 1;
        int size = params.getOrDefault("size", 10) != null ? (int) params.get("size") : 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Specification<MemberExclusiveActivity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 根据活动名称查询
            if (params.containsKey("name") && params.get("name") != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + params.get("name") + "%"));
            }
            // 根据状态查询
            if (params.containsKey("status") && params.get("status") != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), params.get("status")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<MemberExclusiveActivity> activityPage = memberExclusiveActivityRepository.findAll(specification, pageable);
        return activityPage.getContent();
    }

    @Override
    @Transactional
    public MemberExclusiveActivity save(MemberExclusiveActivity activity) {
        // 默认状态为待审核
        if (activity.getStatus() == null) {
            activity.setStatus(0);
        }
        return memberExclusiveActivityRepository.save(activity);
    }

    @Override
    @Transactional
    public MemberExclusiveActivity update(MemberExclusiveActivity activity) {
        MemberExclusiveActivity existingActivity = memberExclusiveActivityRepository.findById(activity.getId()).orElse(null);
        if (existingActivity == null) {
            throw new RuntimeException("会员专享活动不存在");
        }
        // 更新活动信息
        existingActivity.setName(activity.getName());
        existingActivity.setStartTime(activity.getStartTime());
        existingActivity.setEndTime(activity.getEndTime());
        existingActivity.setApplicableLevelIds(activity.getApplicableLevelIds());
        existingActivity.setDescription(activity.getDescription());
        return memberExclusiveActivityRepository.save(existingActivity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        memberExclusiveActivityRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void audit(Long id, Integer status, String remark) {
        MemberExclusiveActivity activity = memberExclusiveActivityRepository.findById(id).orElse(null);
        if (activity == null) {
            throw new RuntimeException("会员专享活动不存在");
        }
        // 只有待审核的活动才能被审核
        if (activity.getStatus() != 0) {
            throw new RuntimeException("活动已审核");
        }
        activity.setStatus(status);
        memberExclusiveActivityRepository.save(activity);
        log.info("会员专享活动{}审核结果：{}", activity.getName(), status == 1 ? "通过" : "拒绝");
    }

    @Override
    public List<MemberExclusiveActivity> findCurrentActivities() {
        LocalDateTime now = LocalDateTime.now();
        return memberExclusiveActivityRepository.findByStatusAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(1, now, now);
    }

    @Override
    public List<MemberExclusiveActivity> findAvailableActivitiesForMember(Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        return memberExclusiveActivityRepository.findByStatusAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(1, now, now);
    }
}
