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
import zz.hujing.baseboot.domain.MarketingReachRecord;
import zz.hujing.baseboot.repository.MarketingReachRecordRepository;
import zz.hujing.baseboot.service.MarketingReachService;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 营销触达服务实现类
 */
@Slf4j
@Service
public class MarketingReachServiceImpl implements MarketingReachService {

    @Autowired
    private MarketingReachRecordRepository marketingReachRecordRepository;

    @Override
    public MarketingReachRecord findById(Long id) {
        return marketingReachRecordRepository.findById(id).orElse(null);
    }

    @Override
    public List<MarketingReachRecord> findByPage(Map<String, Object> params) {
        int page = params.getOrDefault("page", 1) != null ? (int) params.get("page") : 1;
        int size = params.getOrDefault("size", 10) != null ? (int) params.get("size") : 10;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Specification<MarketingReachRecord> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 根据会员ID查询
            if (params.containsKey("memberId") && params.get("memberId") != null) {
                predicates.add(criteriaBuilder.equal(root.get("memberId"), params.get("memberId")));
            }
            // 根据触达类型查询
            if (params.containsKey("reachType") && params.get("reachType") != null) {
                predicates.add(criteriaBuilder.equal(root.get("reachType"), params.get("reachType")));
            }
            // 根据状态查询
            if (params.containsKey("status") && params.get("status") != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), params.get("status")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<MarketingReachRecord> recordPage = marketingReachRecordRepository.findAll(specification, pageable);
        return recordPage.getContent();
    }

    @Override
    @Transactional
    public MarketingReachRecord save(MarketingReachRecord record) {
        // 默认状态为待发送
        if (record.getStatus() == null) {
            record.setStatus(0);
        }
        return marketingReachRecordRepository.save(record);
    }

    @Override
    @Transactional
    public MarketingReachRecord update(MarketingReachRecord record) {
        MarketingReachRecord existingRecord = marketingReachRecordRepository.findById(record.getId()).orElse(null);
        if (existingRecord == null) {
            throw new RuntimeException("营销触达记录不存在");
        }
        // 更新记录信息
        existingRecord.setReachType(record.getReachType());
        existingRecord.setContent(record.getContent());
        existingRecord.setStatus(record.getStatus());
        existingRecord.setSendTime(record.getSendTime());
        existingRecord.setFailReason(record.getFailReason());
        return marketingReachRecordRepository.save(existingRecord);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        marketingReachRecordRepository.deleteById(id);
    }

    @Override
    public List<MarketingReachRecord> findByMemberId(Long memberId) {
        return marketingReachRecordRepository.findByMemberIdOrderByCreateTimeDesc(memberId);
    }

    @Override
    public List<MarketingReachRecord> findPendingRecords() {
        // 这里可以根据需要修改，比如查询所有待发送的记录，不限制触达类型
        return marketingReachRecordRepository.findByReachTypeAndStatusOrderByCreateTimeDesc(null, 0);
    }

    @Override
    @Transactional
    public MarketingReachRecord sendInnerMessage(Long memberId, String content) {
        MarketingReachRecord record = new MarketingReachRecord();
        record.setMemberId(memberId);
        record.setReachType(0); // 站内信
        record.setContent(content);
        record.setStatus(1); // 已发送
        record.setSendTime(LocalDateTime.now());
        MarketingReachRecord savedRecord = marketingReachRecordRepository.save(record);
        log.info("发送站内信给会员{}，内容：{}", memberId, content);
        return savedRecord;
    }

    @Override
    @Transactional
    public MarketingReachRecord sendSmsMessage(Long memberId, String content) {
        MarketingReachRecord record = new MarketingReachRecord();
        record.setMemberId(memberId);
        record.setReachType(1); // 短信
        record.setContent(content);
        record.setStatus(1); // 已发送
        record.setSendTime(LocalDateTime.now());
        MarketingReachRecord savedRecord = marketingReachRecordRepository.save(record);
        log.info("发送短信给会员{}，内容：{}", memberId, content);
        return savedRecord;
    }

    @Override
    @Transactional
    public Map<String, Object> batchSendMessage(List<Long> memberIds, String content, Integer reachType) {
        LocalDateTime now = LocalDateTime.now();
        List<MarketingReachRecord> records = new ArrayList<>();
        for (Long memberId : memberIds) {
            MarketingReachRecord record = new MarketingReachRecord();
            record.setMemberId(memberId);
            record.setReachType(reachType);
            record.setContent(content);
            record.setStatus(1); // 已发送
            record.setSendTime(now);
            records.add(record);
        }
        marketingReachRecordRepository.saveAll(records);
        log.info("批量发送营销消息，触达类型：{}，发送数量：{}", reachType, records.size());
        // 返回发送结果
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", true);
        result.put("count", records.size());
        return result;
    }

    @Override
    @Transactional
    public MarketingReachRecord resendRecord(Long id) {
        MarketingReachRecord record = marketingReachRecordRepository.findById(id).orElse(null);
        if (record == null) {
            throw new RuntimeException("营销触达记录不存在");
        }
        // 重新发送消息
        record.setStatus(1); // 已发送
        record.setSendTime(LocalDateTime.now());
        MarketingReachRecord savedRecord = marketingReachRecordRepository.save(record);
        log.info("重新发送营销触达记录{}，触达类型：{}", id, record.getReachType());
        return savedRecord;
    }
}
