package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.MemberLevelHistory;

import java.util.List;

/**
 * 会员等级历史记录Repository
 */
@Repository
public interface MemberLevelHistoryRepository extends JpaRepository<MemberLevelHistory, Long> {
    
    /**
     * 根据会员ID查询会员等级历史记录
     * @param memberId 会员ID
     * @return 会员等级历史记录列表
     */
    List<MemberLevelHistory> findByMemberId(Long memberId);
}