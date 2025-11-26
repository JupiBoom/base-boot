package zz.hujing.baseboot.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceData {
    private String deviceId;
    private Double temperature;
    private Double humidity;
    private String status;
    private Instant timestamp;
}