package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.MemberLevel;

import java.util.List;

/**
 * 会员等级Repository
 */
@Repository
public interface MemberLevelRepository extends JpaRepository<MemberLevel, Long>, JpaSpecificationExecutor<MemberLevel> {
    /**
     * 根据等级代码查询会员等级
     * @param code 等级代码
     * @return 会员等级信息
     */
    MemberLevel findByCode(String code);

    /**
     * 查询启用的会员等级
     * @return 启用的会员等级列表
     */
    List<MemberLevel> findByStatusOrderByMinGrowthValueAsc(Integer status);
}
