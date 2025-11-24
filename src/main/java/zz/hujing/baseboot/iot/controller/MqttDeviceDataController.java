package zz.hujing.baseboot.iot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zz.hujing.baseboot.iot.config.RabbitMQConfig;
import zz.hujing.baseboot.iot.domain.DeviceData;

import javax.validation.Valid;
import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/api/mqtt/device-data")
public class MqttDeviceDataController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<Void> receiveMqttDeviceData(@Valid @RequestBody DeviceData deviceData) {
        try {
            // 设置时间戳（如果设备没有提供）
            if (deviceData.getTimestamp() == null) {
                deviceData.setTimestamp(Instant.now());
            }

            // 将设备数据发送到RabbitMQ队列
            rabbitTemplate.convertAndSend(RabbitMQConfig.DEVICE_DATA_EXCHANGE,
                    RabbitMQConfig.DEVICE_DATA_ROUTING_KEY, deviceData);

            log.info("Received MQTT device data and sent to RabbitMQ: {}", deviceData);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to process MQTT device data: {}", deviceData, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}