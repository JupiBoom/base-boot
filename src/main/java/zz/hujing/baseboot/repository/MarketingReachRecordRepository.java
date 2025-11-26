package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.MarketingReachRecord;

import java.util.List;

/**
 * 营销触达记录Repository
 */
@Repository
public interface MarketingReachRecordRepository extends JpaRepository<MarketingReachRecord, Long>, JpaSpecificationExecutor<MarketingReachRecord> {
    /**
     * 根据会员ID查询触达记录
     * @param memberId 会员ID
     * @return 触达记录列表
     */
    List<MarketingReachRecord> findByMemberIdOrderByCreateTimeDesc(Long memberId);

    /**
     * 根据触达类型和状态查询
     * @param reachType 触达类型
     * @param status 状态
     * @return 触达记录列表
     */
    List<MarketingReachRecord> findByReachTypeAndStatusOrderByCreateTimeDesc(Integer reachType, Integer status);
}
