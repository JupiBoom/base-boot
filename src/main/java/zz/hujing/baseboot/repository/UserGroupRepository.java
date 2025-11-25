package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.UserGroup;

/**
 * 用户分群Repository
 */
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}