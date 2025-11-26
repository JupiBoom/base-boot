package zz.hujing.baseboot.core.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.domain.DynamicConfig;
import zz.hujing.baseboot.service.DynamicConfigService;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class DeviceOfflineListener {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    private static final String REDIS_DEVICE_LAST_DATA_TIME_PREFIX = "device:last-data-time:";
    private static final String REDIS_DEVICE_STATUS_PREFIX = "device:status:";

    /**
     * 每1分钟检查一次设备离线状态
     */
    @Scheduled(fixedRate = 60000)
    public void checkDeviceOffline() {
        String pattern = REDIS_DEVICE_LAST_DATA_TIME_PREFIX + "*";
        Set<String> keys = redisTemplate.keys(pattern);

        if (keys == null || keys.isEmpty()) {
            return;
        }

        Instant now = Instant.now();
        // 获取当前配置
        DynamicConfig config = dynamicConfigService.getCurrentConfig();
        
        for (String key : keys) {
            String deviceId = key.replace(REDIS_DEVICE_LAST_DATA_TIME_PREFIX, "");
            Instant lastDataTime = (Instant) redisTemplate.opsForValue().get(key);

            if (lastDataTime != null && now.isAfter(lastDataTime.plusMinutes(config.getOfflineThreshold()))) {
                // 设备离线
                String statusKey = REDIS_DEVICE_STATUS_PREFIX + deviceId;
                String currentStatus = (String) redisTemplate.opsForValue().get(statusKey);
                if (!"offline".equals(currentStatus)) {
                    redisTemplate.opsForValue().set(statusKey, "offline");
                    // 触发离线告警
                    triggerOfflineAlarm(deviceId);
                }
            } else {
                // 设备在线
                String statusKey = REDIS_DEVICE_STATUS_PREFIX + deviceId;
                redisTemplate.opsForValue().set(statusKey, "online");
            }
        }
    }

    private void triggerOfflineAlarm(String deviceId) {
        // 这里可以实现离线告警逻辑，比如发送邮件、短信或推送通知
        System.out.println("告警触发：设备 " + deviceId + " 已离线");
    }
}