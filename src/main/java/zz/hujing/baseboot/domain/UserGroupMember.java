package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户分群成员
 */
@Data
@Entity
@Table(name = "user_group_member")
@EntityListeners(AuditingEntityListener.class)
public class UserGroupMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户分群ID
     */
    @Column(nullable = false)
    private Long userGroupId;
    
    /**
     * 会员ID
     */
    @Column(nullable = false)
    private Long memberId;
    
    /**
     * 添加时间
     */
    @CreatedDate
    private LocalDateTime addTime;
}