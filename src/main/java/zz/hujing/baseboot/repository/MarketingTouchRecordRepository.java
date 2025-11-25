package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.MarketingTouchRecord;

import java.util.List;

/**
 * 营销触达记录Repository
 */
@Repository
public interface MarketingTouchRecordRepository extends JpaRepository<MarketingTouchRecord, Long> {
    
    /**
     * 根据会员ID查询触达记录列表
     * @param memberId 会员ID
     * @return 触达记录列表
     */
    List<MarketingTouchRecord> findByMemberId(Long memberId);
}