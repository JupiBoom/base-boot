package zz.hujing.baseboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.MarketingActivity;
import zz.hujing.baseboot.repository.MarketingActivityRepository;
import zz.hujing.baseboot.service.MarketingActivityService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 营销活动服务实现类
 */
@Service
@RequiredArgsConstructor
public class MarketingActivityServiceImpl implements MarketingActivityService {
    
    private final MarketingActivityRepository marketingActivityRepository;
    
    @Override
    public MarketingActivity findById(Long id) {
        return marketingActivityRepository.findById(id).orElse(null);
    }
    
    @Override
    public MarketingActivity save(MarketingActivity activity) {
        // 设置状态为待审核
        activity.setStatus(1);
        return marketingActivityRepository.save(activity);
    }
    
    @Override
    public void auditActivity(Long id, Integer status) {
        MarketingActivity activity = marketingActivityRepository.findById(id).orElseThrow(() -> new RuntimeException("营销活动不存在"));
        activity.setStatus(status);
        marketingActivityRepository.save(activity);
    }
    
    @Override
    public List<MarketingActivity> findActiveActivities() {
        LocalDateTime now = LocalDateTime.now();
        // 查询已审核、开始时间小于等于当前时间、结束时间大于当前时间的营销活动
        return marketingActivityRepository.findByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThan(2, now, now);
    }
    
    @Override
    public List<MarketingActivity> findAvailableActivitiesByMember(Long memberId) {
        // 暂时返回所有正在进行中的活动，后续可根据会员等级进行过滤
        return findActiveActivities();
    }
}