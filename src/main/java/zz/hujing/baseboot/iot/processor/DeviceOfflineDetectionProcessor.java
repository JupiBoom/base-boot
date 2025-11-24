package zz.hujing.baseboot.iot.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.iot.config.DynamicConfig;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@Slf4j
@Component
public class DeviceOfflineDetectionProcessor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DynamicConfig dynamicConfig;

    private static final String DEVICE_LAST_SEEN_KEY_PREFIX = "device_last_seen:";
    private static final String DEVICE_STATUS_KEY_PREFIX = "device_status:";

    public void updateDeviceLastSeen(String deviceId) {
        String lastSeenKey = DEVICE_LAST_SEEN_KEY_PREFIX + deviceId;
        redisTemplate.opsForValue().set(lastSeenKey, Instant.now().toEpochMilli(), Duration.ofMinutes(dynamicConfig.getOfflineThreshold()).plus(Duration.ofMinutes(1)));
    }

    // 每1分钟检测一次设备离线状态
    @Scheduled(fixedRate = 60000)
    public void detectOfflineDevices() {
        log.info("Starting device offline detection...");

        // 获取所有设备的最后在线时间
        Set<String> lastSeenKeys = redisTemplate.keys(DEVICE_LAST_SEEN_KEY_PREFIX + "*");
        if (lastSeenKeys == null || lastSeenKeys.isEmpty()) {
            log.info("No devices found for offline detection.");
            return;
        }

        long currentTime = Instant.now().toEpochMilli();
        for (String key : lastSeenKeys) {
            String deviceId = key.substring(DEVICE_LAST_SEEN_KEY_PREFIX.length());
            Long lastSeenTime = (Long) redisTemplate.opsForValue().get(key);

            if (lastSeenTime == null) {
                // 设备从未在线过，忽略
                continue;
            }

            // 检查设备是否离线
            if (currentTime - lastSeenTime > Duration.ofMinutes(dynamicConfig.getOfflineThreshold()).toMillis()) {
                // 更新设备状态为离线
                String statusKey = DEVICE_STATUS_KEY_PREFIX + deviceId;
                String previousStatus = (String) redisTemplate.opsForValue().get(statusKey);
                if (!"offline".equals(previousStatus)) {
                    redisTemplate.opsForValue().set(statusKey, "offline");
                    log.error("Device {} is offline! Last seen at: {}", deviceId, Instant.ofEpochMilli(lastSeenTime));

                    // TODO: 实现离线告警通知逻辑（如发送邮件、短信等）
                }
            } else {
                // 更新设备状态为在线
                String statusKey = DEVICE_STATUS_KEY_PREFIX + deviceId;
                String previousStatus = (String) redisTemplate.opsForValue().get(statusKey);
                if (!"online".equals(previousStatus)) {
                    redisTemplate.opsForValue().set(statusKey, "online");
                    log.info("Device {} is online.", deviceId);
                }
            }
        }

        log.info("Device offline detection completed successfully.");
    }
}