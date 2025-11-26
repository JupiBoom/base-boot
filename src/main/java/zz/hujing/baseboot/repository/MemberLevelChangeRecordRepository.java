package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.MemberLevelChangeRecord;

import java.util.List;

/**
 * 会员等级变更记录Repository
 */
@Repository
public interface MemberLevelChangeRecordRepository extends JpaRepository<MemberLevelChangeRecord, Long>, JpaSpecificationExecutor<MemberLevelChangeRecord> {
    /**
     * 根据会员ID查询等级变更记录
     * @param memberId 会员ID
     * @return 等级变更记录列表
     */
    List<MemberLevelChangeRecord> findByMemberIdOrderByCreateTimeDesc(Long memberId);
}
