package zz.hujing.baseboot.core.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zz.hujing.baseboot.domain.DeviceData;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Configuration
public class InfluxDBConfig {
    @Value("${influxdb.url:http://localhost:8086}")
    private String url;

    @Value("${influxdb.username:admin}")
    private String username;

    @Value("${influxdb.password:admin}")
    private String password;

    @Value("${influxdb.database:iot_data}")
    private String database;

    @Bean
    public InfluxDB influxDB() {
        InfluxDB influxDB = InfluxDBFactory.connect(url, username, password);
        influxDB.setDatabase(database);
        influxDB.setRetentionPolicy("autogen");
        influxDB.enableBatch(1000, 100, TimeUnit.MILLISECONDS);
        return influxDB;
    }

    /**
     * 将设备数据转换为InfluxDB Point
     * @param deviceData 设备数据
     * @return InfluxDB Point
     */
    public static Point toPoint(DeviceData deviceData) {
        return Point.measurement("device_data")
                .time(deviceData.getTimestamp().toEpochMilli(), TimeUnit.MILLISECONDS)
                .tag("deviceId", deviceData.getDeviceId())
                .addField("temperature", deviceData.getTemperature())
                .addField("humidity", deviceData.getHumidity())
                .addField("status", deviceData.getStatus())
                .addField("rawData", deviceData.getRawData())
                .build();
    }
}