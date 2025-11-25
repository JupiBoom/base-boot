package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zz.hujing.baseboot.domain.enums.MemberLevelEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 会员等级历史记录
 */
@Data
@Entity
@Table(name = "member_level_history")
@EntityListeners(AuditingEntityListener.class)
public class MemberLevelHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 会员ID
     */
    @Column(nullable = false)
    private Long memberId;
    
    /**
     * 变更前等级
     */
    @Enumerated(EnumType.ORDINAL)
    private MemberLevelEnum oldLevel;
    
    /**
     * 变更后等级
     */
    @Enumerated(EnumType.ORDINAL)
    private MemberLevelEnum newLevel;
    
    /**
     * 变更原因
     */
    private String reason;
    
    /**
     * 变更时间
     */
    @CreatedDate
    private LocalDateTime changeTime;
}