package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.Member;

/**
 * 会员Repository
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    /**
     * 根据会员编号查询会员
     * @param memberNo 会员编号
     * @return 会员信息
     */
    Member findByMemberNo(String memberNo);

    /**
     * 根据手机号查询会员
     * @param phone 手机号
     * @return 会员信息
     */
    Member findByPhone(String phone);
}
