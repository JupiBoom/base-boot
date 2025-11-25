package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.UserGroupMember;

import java.util.List;

/**
 * 用户分群成员Repository
 */
@Repository
public interface UserGroupMemberRepository extends JpaRepository<UserGroupMember, Long> {
    
    /**
     * 根据用户分群ID查询成员列表
     * @param userGroupId 用户分群ID
     * @return 成员列表
     */
    List<UserGroupMember> findByUserGroupId(Long userGroupId);
    
    /**
     * 根据会员ID查询所属分群列表
     * @param memberId 会员ID
     * @return 分群列表
     */
    List<UserGroupMember> findByMemberId(Long memberId);
}