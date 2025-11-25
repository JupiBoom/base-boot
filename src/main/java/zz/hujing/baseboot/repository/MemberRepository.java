package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.Member;
import zz.hujing.baseboot.domain.enums.MemberLevelEnum;

import java.util.List;
import java.util.Optional;

/**
 * 会员Repository
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    /**
     * 根据手机号码查询会员
     * @param phone 手机号码
     * @return 会员信息
     */
    Optional<Member> findByPhone(String phone);
    
    /**
     * 根据会员等级查询会员列表
     * @param currentLevel 会员等级
     * @return 会员列表
     */
    List<Member> findByCurrentLevel(MemberLevelEnum currentLevel);
}