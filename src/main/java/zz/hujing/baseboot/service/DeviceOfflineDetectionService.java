package zz.hujing.baseboot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RefreshScope
public class DeviceOfflineDetectionService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${device.offlineThreshold:300000}")
    private long offlineThreshold;

    public DeviceOfflineDetectionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 定期检测设备离线状态（每1分钟执行一次）
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void detectOfflineDevices() {
        try {
            System.out.println("Starting device offline detection...");

            // 获取所有设备的最后活跃时间键
            Set<String> keys = redisTemplate.keys("device:last_active:*");
            if (keys == null || keys.isEmpty()) {
                System.out.println("No devices found for offline detection");
                return;
            }

            long currentTime = Instant.now().toEpochMilli();

            for (String key : keys) {
                String deviceId = key.replace("device:last_active:", "");
                Long lastActiveTime = (Long) redisTemplate.opsForValue().get(key);

                if (lastActiveTime != null && currentTime - lastActiveTime > offlineThreshold) {
                    // 设备离线，触发离线告警
                    triggerOfflineAlarm(deviceId);
                    // 移除离线设备的最后活跃时间记录
                    redisTemplate.delete(key);
                }
            }

            System.out.println("Device offline detection completed");
        } catch (Exception e) {
            System.err.println("Failed to detect offline devices: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 触发设备离线告警
     * @param deviceId 设备ID
     */
    private void triggerOfflineAlarm(String deviceId) {
        String redisKey = "device:offline_alarm_last_time:" + deviceId;
        Long lastAlarmTime = (Long) redisTemplate.opsForValue().get(redisKey);
        long currentTime = Instant.now().toEpochMilli();

        // 告警防抖动：相同设备30秒内不重复告警
        if (lastAlarmTime == null || currentTime - lastAlarmTime > 30 * 1000) {
            System.out.println("Offline alarm triggered for device " + deviceId);
            // 这里可以添加告警通知逻辑，比如发送邮件、短信或推送通知
            // 更新最后告警时间
            redisTemplate.opsForValue().set(redisKey, currentTime, 30, TimeUnit.SECONDS);
        }
    }
}