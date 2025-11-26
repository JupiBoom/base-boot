package zz.hujing.baseboot.service.impl;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.DeviceData;
import zz.hujing.baseboot.domain.DynamicConfig;
import zz.hujing.baseboot.service.DeviceDataService;
import zz.hujing.baseboot.service.DynamicConfigService;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class DeviceDataServiceImpl implements DeviceDataService {

    @Autowired
    private InfluxDB influxDB;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    private static final String INFLUXDB_MEASUREMENT = "device_data";
    private static final String REDIS_EXCEPTION_COUNT_PREFIX = "device:exception:count:";
    private static final String REDIS_ALARM_LAST_TIME_PREFIX = "device:alarm:last:";

    private static final String REDIS_DEVICE_LAST_DATA_TIME_PREFIX = "device:last-data-time:";

    @Override
    public void processDeviceData(DeviceData deviceData) {
        // 数据清洗
        if (!isValidData(deviceData)) {
            return;
        }

        // 存储原始数据到InfluxDB
        saveToInfluxDB(deviceData);

        // 更新设备最后数据时间
        updateDeviceLastDataTime(deviceData);

        // 异常检测
        checkDeviceException(deviceData);
    }

    private void updateDeviceLastDataTime(DeviceData deviceData) {
        String key = REDIS_DEVICE_LAST_DATA_TIME_PREFIX + deviceData.getDeviceId();
        redisTemplate.opsForValue().set(key, deviceData.getTimestamp(), 10, TimeUnit.MINUTES);
    }

    private boolean isValidData(DeviceData deviceData) {
        if (deviceData == null || deviceData.getDeviceId() == null || deviceData.getTimestamp() == null) {
            return false;
        }
        // 温度范围校验：-40~125℃
        if (deviceData.getTemperature() != null && (deviceData.getTemperature() < -40 || deviceData.getTemperature() > 125)) {
            return false;
        }
        // 湿度范围校验：0~100%
        if (deviceData.getHumidity() != null && (deviceData.getHumidity() < 0 || deviceData.getHumidity() > 100)) {
            return false;
        }
        return true;
    }

    private void saveToInfluxDB(DeviceData deviceData) {
        Point point = Point.measurement(INFLUXDB_MEASUREMENT)
                .time(deviceData.getTimestamp().toEpochMilli(), TimeUnit.MILLISECONDS)
                .tag("deviceId", deviceData.getDeviceId())
                .addField("temperature", deviceData.getTemperature())
                .addField("humidity", deviceData.getHumidity())
                .addField("status", deviceData.getStatus())
                .build();
        influxDB.write(point);
    }

    private void checkDeviceException(DeviceData deviceData) {
        String deviceId = deviceData.getDeviceId();
        String exceptionCountKey = REDIS_EXCEPTION_COUNT_PREFIX + deviceId;
        String alarmLastTimeKey = REDIS_ALARM_LAST_TIME_PREFIX + deviceId;

        // 检查设备状态是否异常
        boolean isException = "error".equalsIgnoreCase(deviceData.getStatus()) || 
                (deviceData.getTemperature() != null && (deviceData.getTemperature() < -30 || deviceData.getTemperature() > 110)) ||
                (deviceData.getHumidity() != null && (deviceData.getHumidity() < 10 || deviceData.getHumidity() > 90));

        if (isException) {
            // 异常计数加1
            Long count = redisTemplate.opsForValue().increment(exceptionCountKey);
            if (count == null) count = 1L;

            // 获取当前配置
            DynamicConfig config = dynamicConfigService.getCurrentConfig();

            // 设置过期时间，防止内存溢出
            redisTemplate.expire(exceptionCountKey, config.getOfflineThreshold(), TimeUnit.MINUTES);

            // 连续异常次数达到阈值
            if (count >= config.getAlarmThreshold()) {
                // 检查是否在告警防抖动时间内
                Instant lastAlarmTime = (Instant) redisTemplate.opsForValue().get(alarmLastTimeKey);
                if (lastAlarmTime == null || Instant.now().isAfter(lastAlarmTime.plusSeconds(config.getAlarmCooldown()))) {
                    // 触发告警
                    triggerAlarm(deviceData);
                    // 记录最后告警时间
                    redisTemplate.opsForValue().set(alarmLastTimeKey, Instant.now(), config.getAlarmCooldown(), TimeUnit.SECONDS);
                }
            }
        } else {
            // 状态正常，重置异常计数
            redisTemplate.delete(exceptionCountKey);
        }
    }

    private void triggerAlarm(DeviceData deviceData) {
        // 这里可以实现告警逻辑，比如发送邮件、短信或推送通知
        System.out.println("告警触发：设备 " + deviceData.getDeviceId() + " 出现异常，温度：" + deviceData.getTemperature() + "℃，湿度：" + deviceData.getHumidity() + "%");
    }
}