package zz.hujing.baseboot.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 预警记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "alert_record")
public class AlertRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 产品ID
     */
    @Column(name = "product_id", nullable = false)
    private Long productId;

    /**
     * 预警类型（1：库存不足，2：库存周转率异常，3：呆滞库存）
     */
    @Column(name = "alert_type", nullable = false)
    private Integer alertType;

    /**
     * 预警级别（1：提醒，2：警告，3：紧急）
     */
    @Column(name = "alert_level", nullable = false)
    private Integer alertLevel;

    /**
     * 预警内容
     */
    @Column(name = "alert_content", nullable = false)
    private String alertContent;

    /**
     * 处理状态（0：未处理，1：已处理）
     */
    @Column(name = "handle_status", nullable = false)
    private Integer handleStatus;

    /**
     * 处理人
     */
    @Column(name = "handler")
    private String handler;

    /**
     * 处理时间
     */
    @Column(name = "handle_time")
    private LocalDateTime handleTime;

    /**
     * 处理备注
     */
    @Column(name = "handle_remark")
    private String handleRemark;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
}
