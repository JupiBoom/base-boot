package zz.hujing.baseboot.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zz.hujing.baseboot.core.config.RabbitMQConfig;

@RestController
@RequestMapping("/api/device")
public class DeviceDataController {
    private final RabbitTemplate rabbitTemplate;

    public DeviceDataController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 接收设备通过HTTP协议上报的数据
     * @param data 设备数据（JSON格式）
     * @return 响应结果
     */
    @PostMapping("/data")
    public ResponseEntity<String> receiveDeviceData(@RequestBody String data) {
        try {
            System.out.println("HTTP device data received: " + data);
            // 将HTTP请求的数据转发到RabbitMQ
            rabbitTemplate.convertAndSend(RabbitMQConfig.DEVICE_DATA_EXCHANGE,
                    RabbitMQConfig.DEVICE_DATA_ROUTING_KEY, data);
            return ResponseEntity.ok("Data received successfully");
        } catch (Exception e) {
            System.err.println("Failed to process device data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process data");
        }
    }
}