package zz.hujing.baseboot.iot.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.iot.domain.DeviceData;
import zz.hujing.baseboot.iot.service.DeviceDataService;

@Slf4j
@Component
public class DeviceDataProcessor {

    @Autowired
    private DeviceDataService deviceDataService;

    @RabbitListener(queues = "device_data_queue")
    public void processDeviceData(DeviceData deviceData) {
        try {
            log.info("Received device data from RabbitMQ: {}", deviceData);
            deviceDataService.processDeviceData(deviceData);
        } catch (Exception e) {
            log.error("Failed to process device data from RabbitMQ: {}", deviceData, e);
        }
    }
}