package zz.hujing.baseboot.iot.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.iot.config.DynamicConfig;
import zz.hujing.baseboot.iot.domain.DeviceData;
import zz.hujing.baseboot.iot.service.DeviceDataService;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class DeviceAnomalyDetectionProcessor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DeviceDataService deviceDataService;

    @Autowired
    private DynamicConfig dynamicConfig;

    private static final String ANOMALY_COUNT_KEY_PREFIX = "device_anomaly_count:";
    private static final String ANOMALY_ALERT_KEY_PREFIX = "device_anomaly_alert:";

    public void detectAnomaly(DeviceData deviceData) {
        // 检查设备状态是否异常
        if (isAnomalous(deviceData)) {
            // 增加异常计数
            String anomalyCountKey = ANOMALY_COUNT_KEY_PREFIX + deviceData.getDeviceId();
            AtomicInteger anomalyCount = (AtomicInteger) redisTemplate.opsForValue().get(anomalyCountKey);
            if (anomalyCount == null) {
                anomalyCount = new AtomicInteger(0);
            }
            int count = anomalyCount.incrementAndGet();
            redisTemplate.opsForValue().set(anomalyCountKey, anomalyCount, Duration.ofMinutes(5));

            // 如果连续异常次数达到阈值，触发告警
            if (count >= dynamicConfig.getAnomalyThreshold()) {
                triggerAlert(deviceData);
            }
        } else {
            // 重置异常计数
            String anomalyCountKey = ANOMALY_COUNT_KEY_PREFIX + deviceData.getDeviceId();
            redisTemplate.delete(anomalyCountKey);
        }
    }

    private boolean isAnomalous(DeviceData deviceData) {
        // 简单的异常检测逻辑：检查设备状态是否为"error"或温度是否超出范围
        return "error".equals(deviceData.getStatus()) ||
                deviceData.getTemperature() < -40 || deviceData.getTemperature() > 125 ||
                deviceData.getHumidity() < 0 || deviceData.getHumidity() > 100;
    }

    private void triggerAlert(DeviceData deviceData) {
        String alertKey = ANOMALY_ALERT_KEY_PREFIX + deviceData.getDeviceId();

        // 检查是否在告警冷却期内
        if (redisTemplate.hasKey(alertKey)) {
            log.info("Alert for device {} is in cooldown period, skipping...", deviceData.getDeviceId());
            return;
        }

        // 触发告警
        log.error("Device {} is anomalous! Temperature: {}, Humidity: {}, Status: {}",
                deviceData.getDeviceId(), deviceData.getTemperature(), deviceData.getHumidity(), deviceData.getStatus());

        // 设置告警冷却期
        redisTemplate.opsForValue().set(alertKey, true, Duration.ofSeconds(dynamicConfig.getAlertCooldown()));

        // TODO: 实现告警通知逻辑（如发送邮件、短信等）
    }
}