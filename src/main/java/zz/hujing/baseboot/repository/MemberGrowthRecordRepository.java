package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.MemberGrowthRecord;

import java.util.List;

/**
 * 会员成长值记录Repository
 */
@Repository
public interface MemberGrowthRecordRepository extends JpaRepository<MemberGrowthRecord, Long>, JpaSpecificationExecutor<MemberGrowthRecord> {
    /**
     * 根据会员ID查询成长值记录
     * @param memberId 会员ID
     * @return 成长值记录列表
     */
    List<MemberGrowthRecord> findByMemberIdOrderByCreateTimeDesc(Long memberId);
}
