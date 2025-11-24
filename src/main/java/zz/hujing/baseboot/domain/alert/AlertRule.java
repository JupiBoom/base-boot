package zz.hujing.baseboot.domain.alert;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_rule")
public class AlertRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Column(name = "rule_type", nullable = false)
    private String ruleType;

    @Column(name = "threshold_value", nullable = false)
    private Double thresholdValue;

    @Column(name = "alert_level", nullable = false)
    private String alertLevel;

    @Column(name = "description")
    private String description;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();

    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime = LocalDateTime.now();
}