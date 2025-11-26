package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 营销触达记录实体类
 */
@Data
@Entity
@Table(name = "marketing_reach_record")
@EntityListeners(AuditingEntityListener.class)
public class MarketingReachRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会员ID
     */
    @Column(nullable = false)
    private Long memberId;

    /**
     * 触达类型（0：站内信，1：短信推送）
     */
    @Column(nullable = false)
    private Integer reachType;

    /**
     * 触达内容
     */
    @Column(nullable = false)
    private String content;

    /**
     * 触达状态（0：待发送，1：发送成功，2：发送失败）
     */
    @Column(nullable = false)
    private Integer status;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;

    /**
     * 会员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", insertable = false, updatable = false)
    private Member member;
}
