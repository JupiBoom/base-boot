package zz.hujing.baseboot.iot.service;

import zz.hujing.baseboot.iot.domain.DeviceData;

public interface DeviceDataService {
    void processDeviceData(DeviceData deviceData);
    void saveDeviceData(DeviceData deviceData);
    void saveAggregatedData(String deviceId, Double avgTemperature, Double avgHumidity, long timestamp);
}