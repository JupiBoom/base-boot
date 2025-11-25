package zz.hujing.baseboot.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zz.hujing.baseboot.domain.enums.MemberLevelEnum;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员实体
 */
@Data
@Entity
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 会员编号
     */
    @Column(unique = true, nullable = false)
    private String memberNo;
    
    /**
     * 会员名称
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * 手机号码
     */
    @Column(unique = true, nullable = false)
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 性别
     */
    private Integer gender;
    
    /**
     * 生日
     */
    private LocalDateTime birthday;
    
    /**
     * 注册时间
     */
    @CreatedDate
    private LocalDateTime registerTime;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 当前成长值
     */
    private Integer growthValue = 0;
    
    /**
     * 当前会员等级
     */
    @Enumerated(EnumType.ORDINAL)
    private MemberLevelEnum currentLevel = MemberLevelEnum.NORMAL;
    
    /**
     * 偏好标签（JSON格式存储）
     */
    private String preferenceTags;
    
    /**
     * 状态（1：正常，0：冻结）
     */
    private Integer status = 1;
    
    /**
     * 更新时间
     */
    @LastModifiedDate
    private LocalDateTime updateTime;
}