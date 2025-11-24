package zz.hujing.baseboot.iot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zz.hujing.baseboot.iot.domain.DeviceData;
import zz.hujing.baseboot.iot.service.DeviceDataService;

import javax.validation.Valid;
import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/api/device-data")
public class DeviceDataController {

    @Autowired
    private DeviceDataService deviceDataService;

    @PostMapping
    public ResponseEntity<Void> receiveDeviceData(@Valid @RequestBody DeviceData deviceData) {
        try {
            // 设置时间戳（如果设备没有提供）
            if (deviceData.getTimestamp() == null) {
                deviceData.setTimestamp(Instant.now());
            }

            // 处理设备数据
            deviceDataService.processDeviceData(deviceData);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to process device data: {}", deviceData, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}