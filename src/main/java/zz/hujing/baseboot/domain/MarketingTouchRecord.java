package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zz.hujing.baseboot.domain.enums.MarketingTouchTypeEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 营销触达记录
 */
@Data
@Entity
@Table(name = "marketing_touch_record")
@EntityListeners(AuditingEntityListener.class)
public class MarketingTouchRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 会员ID
     */
    @Column(nullable = false)
    private Long memberId;
    
    /**
     * 触达类型
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private MarketingTouchTypeEnum type;
    
    /**
     * 触达标题
     */
    @Column(nullable = false)
    private String title;
    
    /**
     * 触达内容
     */
    @Column(nullable = false)
    private String content;
    
    /**
     * 触达时间
     */
    @CreatedDate
    private LocalDateTime touchTime;
    
    /**
     * 触达状态（1：成功，0：失败）
     */
    @Column(nullable = false)
    private Integer status = 1;
    
    /**
     * 失败原因
     */
    private String failReason;
    
    /**
     * 点击时间
     */
    private LocalDateTime clickTime;
}