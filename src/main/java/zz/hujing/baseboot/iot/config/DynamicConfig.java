package zz.hujing.baseboot.iot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "iot")
public class DynamicConfig {
    // 数据采集频率（秒）
    private int collectionFrequency = 30;

    // 告警阈值：连续异常次数
    private int anomalyThreshold = 3;

    // 告警防抖动时间（秒）
    private int alertCooldown = 30;

    // 设备离线阈值（分钟）
    private int offlineThreshold = 5;
}
