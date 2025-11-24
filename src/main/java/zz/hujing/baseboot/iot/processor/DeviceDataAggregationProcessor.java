package zz.hujing.baseboot.iot.processor;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.iot.service.DeviceDataService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DeviceDataAggregationProcessor {

    @Autowired
    private InfluxDBClient influxDBClient;

    @Autowired
    private DeviceDataService deviceDataService;

    @Value("${influxdb.bucket}")
    private String bucket;

    @Value("${influxdb.org}")
    private static String org;

    // 每5分钟聚合一次数据
    @Scheduled(fixedRate = 300000)
    public void aggregateDeviceData() {
        log.info("Starting device data aggregation...");

        // 获取过去5分钟的数据
        Instant endTime = Instant.now();
        Instant startTime = endTime.minus(5, ChronoUnit.MINUTES);

        // 查询过去5分钟内的所有设备数据
        String query = String.format(
                "from(bucket: \"%s\") |> range(start: %d, stop: %d) |> filter(fn: (r) => r._measurement == \"device_data\")",
                bucket, startTime.toEpochMilli() * 1000000, endTime.toEpochMilli() * 1000000
        );

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(query, org);

        // 按设备ID分组
        Map<String, List<FluxRecord>> recordsByDevice = tables.stream()
                .flatMap(table -> table.getRecords().stream())
                .collect(Collectors.groupingBy(record -> record.getValueByKey("device_id").toString()));

        // 计算每个设备的平均值
        for (Map.Entry<String, List<FluxRecord>> entry : recordsByDevice.entrySet()) {
            String deviceId = entry.getKey();
            List<FluxRecord> records = entry.getValue();

            // 按字段分组
            Map<String, List<FluxRecord>> recordsByField = records.stream()
                    .collect(Collectors.groupingBy(record -> record.getField()));

            // 计算温度平均值
            Double avgTemperature = recordsByField.getOrDefault("temperature", List.of())
                    .stream()
                    .mapToDouble(record -> (Double) record.getValue())
                    .average()
                    .orElse(0.0);

            // 计算湿度平均值
            Double avgHumidity = recordsByField.getOrDefault("humidity", List.of())
                    .stream()
                    .mapToDouble(record -> (Double) record.getValue())
                    .average()
                    .orElse(0.0);

            // 保存聚合结果
            deviceDataService.saveAggregatedData(deviceId, avgTemperature, avgHumidity, endTime.toEpochMilli() * 1000000);
        }

        log.info("Device data aggregation completed successfully.");
    }
}