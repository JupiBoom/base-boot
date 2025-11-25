package zz.hujing.baseboot.service;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class DataAggregationService {
    private final InfluxDB influxDB;

    public DataAggregationService(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    /**
     * 定期聚合设备数据（每5分钟执行一次）
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void aggregateDeviceData() {
        try {
            System.out.println("Starting data aggregation...");

            // 查询最近5分钟内的所有设备数据
            long endTime = Instant.now().toEpochMilli();
            long startTime = endTime - 5 * 60 * 1000;

            String query = "SELECT mean(temperature) as avg_temperature, " +
                    "mean(humidity) as avg_humidity, " +
                    "last(status) as last_status " +
                    "FROM device_data " +
                    "WHERE time >= " + startTime + "ms AND time < " + endTime + "ms " +
                    "GROUP BY deviceId";

            QueryResult queryResult = influxDB.query(new Query(query, "iot_data"));

            // 处理查询结果
            if (queryResult.getResults() != null && !queryResult.getResults().isEmpty()) {
                QueryResult.Result result = queryResult.getResults().get(0);
                if (result.getSeries() != null && !result.getSeries().isEmpty()) {
                    for (QueryResult.Series series : result.getSeries()) {
                        String deviceId = series.getTags().get("deviceId");
                        List<List<Object>> values = series.getValues();
                        if (values != null && !values.isEmpty()) {
                            List<Object> value = values.get(0);
                            Double avgTemperature = (Double) value.get(1);
                            Double avgHumidity = (Double) value.get(2);
                            Integer lastStatus = (Integer) value.get(3);

                            // 存储聚合结果到InfluxDB
                            saveAggregatedData(deviceId, avgTemperature, avgHumidity, lastStatus, endTime);
                        }
                    }
                }
            }

            System.out.println("Data aggregation completed");
        } catch (Exception e) {
            System.err.println("Failed to aggregate device data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 保存聚合数据到InfluxDB
     * @param deviceId 设备ID
     * @param avgTemperature 平均温度
     * @param avgHumidity 平均湿度
     * @param lastStatus 最后状态
     * @param timestamp 时间戳
     */
    private void saveAggregatedData(String deviceId, Double avgTemperature, Double avgHumidity,
                                    Integer lastStatus, long timestamp) {
        Point point = Point.measurement("device_data_aggregated")
                .time(timestamp, TimeUnit.MILLISECONDS)
                .tag("deviceId", deviceId)
                .addField("avg_temperature", avgTemperature)
                .addField("avg_humidity", avgHumidity)
                .addField("last_status", lastStatus)
                .build();

        influxDB.write(point);
        System.out.println("Aggregated data saved for device " + deviceId + ": avg_temperature=" + avgTemperature + ", avg_humidity=" + avgHumidity + ", last_status=" + lastStatus);
    }
}