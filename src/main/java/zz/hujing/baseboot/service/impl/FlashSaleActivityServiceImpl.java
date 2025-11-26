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
import zz.hujing.baseboot.domain.FlashSaleActivity;
import zz.hujing.baseboot.domain.FlashSaleActivityProduct;
import zz.hujing.baseboot.repository.FlashSaleActivityProductRepository;
import zz.hujing.baseboot.repository.FlashSaleActivityRepository;
import zz.hujing.baseboot.service.FlashSaleActivityService;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 限时折扣活动服务实现类
 */
@Slf4j
@Service
public class FlashSaleActivityServiceImpl implements FlashSaleActivityService {

    @Autowired
    private FlashSaleActivityRepository flashSaleActivityRepository;

    @Autowired
    private FlashSaleActivityProductRepository flashSaleActivityProductRepository;

    @Override
    public FlashSaleActivity findById(Long id) {
        return flashSaleActivityRepository.findById(id).orElse(null);
    }

    @Override
    public List<FlashSaleActivity> findByPage(Map<String, Object> params) {
        int page = params.getOrDefault("page", 1) != null ? (int) params.get("page") : 1;
        int size = params.getOrDefault("size", 10) != null ? (int) params.get("size") : 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Specification<FlashSaleActivity> specification = (root, query, criteriaBuilder) -> {
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

        Page<FlashSaleActivity> activityPage = flashSaleActivityRepository.findAll(specification, pageable);
        return activityPage.getContent();
    }

    @Override
    @Transactional
    public FlashSaleActivity save(FlashSaleActivity activity) {
        // 默认状态为待审核
        if (activity.getStatus() == null) {
            activity.setStatus(0);
        }
        return flashSaleActivityRepository.save(activity);
    }

    @Override
    @Transactional
    public FlashSaleActivity update(FlashSaleActivity activity) {
        FlashSaleActivity existingActivity = flashSaleActivityRepository.findById(activity.getId()).orElse(null);
        if (existingActivity == null) {
            throw new RuntimeException("限时折扣活动不存在");
        }
        // 更新活动信息
        existingActivity.setName(activity.getName());
        existingActivity.setStartTime(activity.getStartTime());
        existingActivity.setEndTime(activity.getEndTime());
        existingActivity.setDescription(activity.getDescription());
        return flashSaleActivityRepository.save(existingActivity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        flashSaleActivityRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void audit(Long id, Integer status, String remark) {
        FlashSaleActivity activity = flashSaleActivityRepository.findById(id).orElse(null);
        if (activity == null) {
            throw new RuntimeException("限时折扣活动不存在");
        }
        // 只有待审核的活动才能被审核
        if (activity.getStatus() != 0) {
            throw new RuntimeException("活动已审核");
        }
        activity.setStatus(status);
        flashSaleActivityRepository.save(activity);
        log.info("限时折扣活动{}审核结果：{}", activity.getName(), status == 1 ? "通过" : "拒绝");
    }

    @Override
    public List<FlashSaleActivity> findCurrentActivities() {
        LocalDateTime now = LocalDateTime.now();
        return flashSaleActivityRepository.findByStatusAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(1, now, now);
    }

    @Override
    public List<FlashSaleActivity> findUpcomingActivities() {
        LocalDateTime now = LocalDateTime.now();
        return flashSaleActivityRepository.findByStatusOrderByStartTimeDesc(1);
    }

    @Override
    public List<FlashSaleActivityProduct> findActivityProducts(Long activityId) {
        return flashSaleActivityProductRepository.findByActivityIdOrderByCreateTimeDesc(activityId);
    }

    @Override
    @Transactional
    public FlashSaleActivityProduct saveActivityProduct(FlashSaleActivityProduct activityProduct) {
        // 检查活动是否存在且已审核通过
        FlashSaleActivity activity = flashSaleActivityRepository.findById(activityProduct.getActivityId()).orElse(null);
        if (activity == null) {
            throw new RuntimeException("限时折扣活动不存在");
        }
        if (activity.getStatus() != 1) {
            throw new RuntimeException("活动未审核通过");
        }

        // 检查商品是否已经在活动中
        FlashSaleActivityProduct existingProduct = flashSaleActivityProductRepository.findByActivityIdAndProductId(activityProduct.getActivityId(), activityProduct.getProductId());
        if (existingProduct != null) {
            throw new RuntimeException("商品已经在活动中");
        }

        return flashSaleActivityProductRepository.save(activityProduct);
    }

    @Override
    @Transactional
    public void deleteActivityProduct(Long id) {
        flashSaleActivityProductRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean deductActivityProductStock(Long activityProductId, Integer quantity) {
        FlashSaleActivityProduct activityProduct = flashSaleActivityProductRepository.findById(activityProductId).orElse(null);
        if (activityProduct == null) {
            throw new RuntimeException("限时折扣活动商品不存在");
        }

        // 检查库存是否充足
        if (activityProduct.getActivityStock() < quantity) {
            throw new RuntimeException("商品库存不足");
        }

        // 扣减库存
        activityProduct.setActivityStock(activityProduct.getActivityStock() - quantity);
        flashSaleActivityProductRepository.save(activityProduct);

        log.info("限时折扣活动商品{}库存扣减{}，剩余库存：{}", activityProduct.getProductId(), quantity, activityProduct.getActivityStock());
        return true;
    }
}
