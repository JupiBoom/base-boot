package zz.hujing.baseboot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.core.config.InfluxDBConfig;
import zz.hujing.baseboot.core.config.RabbitMQConfig;
import zz.hujing.baseboot.domain.DeviceData;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class DeviceDataService {
    private final InfluxDB influxDB;
    private final RedisTemplate<String, Object> redisTemplate;

    public DeviceDataService(InfluxDB influxDB, RedisTemplate<String, Object> redisTemplate) {
        this.influxDB = influxDB;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 从RabbitMQ队列接收设备数据并处理
     * @param message 设备数据消息
     */
    @RabbitListener(queues = RabbitMQConfig.DEVICE_DATA_QUEUE)
    public void processDeviceData(String message) {
        try {
            System.out.println("Processing device data: " + message);

            // 解析JSON数据
            JSONObject jsonObject = JSON.parseObject(message);
            DeviceData deviceData = new DeviceData();
            deviceData.setDeviceId(jsonObject.getString("deviceId"));
            deviceData.setTemperature(jsonObject.getDouble("temperature"));
            deviceData.setHumidity(jsonObject.getDouble("humidity"));
            deviceData.setStatus(jsonObject.getInteger("status"));
            deviceData.setTimestamp(Instant.now());
            deviceData.setRawData(message);

            // 数据清洗
            if (!isValidData(deviceData)) {
                System.out.println("Invalid device data: " + message);
                return;
            }

            // 存储原始数据到InfluxDB
            Point point = InfluxDBConfig.toPoint(deviceData);
            influxDB.write(point);

            // 更新设备最后活跃时间
            updateDeviceLastActiveTime(deviceData.getDeviceId());

            // 异常检测
            checkDeviceException(deviceData);

        } catch (Exception e) {
            System.err.println("Failed to process device data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 验证设备数据是否有效
     * @param deviceData 设备数据
     * @return 是否有效
     */
    private boolean isValidData(DeviceData deviceData) {
        if (deviceData.getDeviceId() == null || deviceData.getDeviceId().isEmpty()) {
            return false;
        }
        if (deviceData.getTemperature() == null || 
            deviceData.getTemperature() < -40 || deviceData.getTemperature() > 125) {
            return false;
        }
        if (deviceData.getHumidity() == null || 
            deviceData.getHumidity() < 0 || deviceData.getHumidity() > 100) {
            return false;
        }
        if (deviceData.getStatus() == null || 
            (deviceData.getStatus() != 0 && deviceData.getStatus() != 1)) {
            return false;
        }
        return true;
    }

    /**
     * 更新设备最后活跃时间
     * @param deviceId 设备ID
     */
    private void updateDeviceLastActiveTime(String deviceId) {
        String redisKey = "device:last_active:" + deviceId;
        redisTemplate.opsForValue().set(redisKey, Instant.now().toEpochMilli(), 5, TimeUnit.MINUTES);
    }

    /**
     * 检测设备异常状态
     * @param deviceData 设备数据
     */
    private void checkDeviceException(DeviceData deviceData) {
        String deviceId = deviceData.getDeviceId();
        String redisKey = "device:exception_count:" + deviceId;

        if (deviceData.getStatus() == 1) {
            // 设备状态异常，增加异常计数
            Long count = redisTemplate.opsForValue().increment(redisKey);
            if (count != null && count >= 3) {
                // 连续3次异常，触发告警
                triggerAlarm(deviceId, "Device status abnormal for 3 consecutive times");
                // 重置异常计数
                redisTemplate.delete(redisKey);
            } else {
                // 设置计数过期时间为5分钟
                redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
            }
        } else {
            // 设备状态正常，重置异常计数
            redisTemplate.delete(redisKey);
        }
    }

    /**
     * 触发设备告警
     * @param deviceId 设备ID
     * @param message 告警消息
     */
    private void triggerAlarm(String deviceId, String message) {
        String redisKey = "device:alarm_last_time:" + deviceId;
        Long lastAlarmTime = (Long) redisTemplate.opsForValue().get(redisKey);
        long currentTime = Instant.now().toEpochMilli();

        // 告警防抖动：相同设备30秒内不重复告警
        if (lastAlarmTime == null || currentTime - lastAlarmTime > 30 * 1000) {
            System.out.println("Alarm triggered for device " + deviceId + ": " + message);
            // 这里可以添加告警通知逻辑，比如发送邮件、短信或推送通知
            // 更新最后告警时间
            redisTemplate.opsForValue().set(redisKey, currentTime, 30, TimeUnit.SECONDS);
        }
    }
}