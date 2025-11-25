package zz.hujing.baseboot.domain;

import lombok.Data;
import java.time.Instant;

@Data
public class DeviceData {
    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 温度值（℃）
     */
    private Double temperature;

    /**
     * 湿度值（%）
     */
    private Double humidity;

    /**
     * 设备状态（0:正常, 1:异常）
     */
    private Integer status;

    /**
     * 上报时间
     */
    private Instant timestamp;

    /**
     * 原始数据内容
     */
    private String rawData;
}