package zz.hujing.baseboot.iot.domain;

import lombok.Data;
import java.time.Instant;

@Data
public class DeviceData {
    private String deviceId;
    private Double temperature;
    private Double humidity;
    private String status;
    private Instant timestamp;
}