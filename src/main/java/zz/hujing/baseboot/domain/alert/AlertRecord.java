package zz.hujing.baseboot.domain.alert;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_record")
public class AlertRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "alert_type", nullable = false)
    private String alertType;

    @Column(name = "alert_level", nullable = false)
    private String alertLevel;

    @Column(name = "alert_message", nullable = false)
    private String alertMessage;

    @Column(name = "current_value", nullable = false)
    private Double currentValue;

    @Column(name = "threshold_value", nullable = false)
    private Double thresholdValue;

    @Column(name = "is_handled", nullable = false)
    private Boolean isHandled = false;

    @Column(name = "handled_by")
    private String handledBy;

    @Column(name = "handled_time")
    private LocalDateTime handledTime;

    @Column(name = "handled_note")
    private String handledNote;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();
}