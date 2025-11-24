package zz.hujing.baseboot.iot.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.iot.domain.DeviceData;
import zz.hujing.baseboot.iot.processor.DeviceAnomalyDetectionProcessor;
import zz.hujing.baseboot.iot.processor.DeviceOfflineDetectionProcessor;
import zz.hujing.baseboot.iot.service.DeviceDataService;

@Slf4j
@Service
public class DeviceDataServiceImpl implements DeviceDataService {

    @Autowired
    private InfluxDBClient influxDBClient;

    @Autowired
    private DeviceAnomalyDetectionProcessor anomalyDetectionProcessor;

    @Autowired
    private DeviceOfflineDetectionProcessor offlineDetectionProcessor;

    @Value("${influxdb.bucket}")
    private String bucket;

    @Value("${influxdb.org}")
    private static String org;

    @Override
    public void processDeviceData(DeviceData deviceData) {
        // 数据清洗
        if (!isValidDeviceData(deviceData)) {
            log.warn("Invalid device data: {}", deviceData);
            return;
        }

        // 保存原始数据
        saveDeviceData(deviceData);

        // 异常检测
        anomalyDetectionProcessor.detectAnomaly(deviceData);

        // 更新设备最后在线时间
        offlineDetectionProcessor.updateDeviceLastSeen(deviceData.getDeviceId());
    }

    @Override
    public void saveDeviceData(DeviceData deviceData) {
        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            Point point = Point.measurement("device_data")
                    .addTag("device_id", deviceData.getDeviceId())
                    .addField("temperature", deviceData.getTemperature())
                    .addField("humidity", deviceData.getHumidity())
                    .addField("status", deviceData.getStatus())
                    .time(deviceData.getTimestamp(), WritePrecision.NS);

            writeApi.writePoint(bucket, org, point);
            log.info("Saved device data: {}", deviceData);
        } catch (Exception e) {
            log.error("Failed to save device data: {}", deviceData, e);
        }
    }

    @Override
    public void saveAggregatedData(String deviceId, Double avgTemperature, Double avgHumidity, long timestamp) {
        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            Point point = Point.measurement("aggregated_device_data")
                    .addTag("device_id", deviceId)
                    .addField("avg_temperature", avgTemperature)
                    .addField("avg_humidity", avgHumidity)
                    .time(timestamp, WritePrecision.NS);

            writeApi.writePoint(bucket, org, point);
            log.info("Saved aggregated device data for device {}: avgTemperature={}, avgHumidity={}",
                    deviceId, avgTemperature, avgHumidity);
        } catch (Exception e) {
            log.error("Failed to save aggregated device data for device {}", deviceId, e);
        }
    }

    private boolean isValidDeviceData(DeviceData deviceData) {
        if (deviceData.getDeviceId() == null || deviceData.getDeviceId().isEmpty()) {
            return false;
        }

        if (deviceData.getTemperature() == null || deviceData.getTemperature() < -40 || deviceData.getTemperature() > 125) {
            return false;
        }

        if (deviceData.getHumidity() == null || deviceData.getHumidity() < 0 || deviceData.getHumidity() > 100) {
            return false;
        }

        if (deviceData.getStatus() == null || deviceData.getStatus().isEmpty()) {
            return false;
        }

        return deviceData.getTimestamp() != null;
    }
}