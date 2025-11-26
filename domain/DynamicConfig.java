package zz.hujing.baseboot.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicConfig {
    private Integer dataCollectionFrequency; // 数据采集频率，单位：秒
    private Integer alarmThreshold; // 告警阈值，连续异常次数
    private Integer alarmCooldown; // 告警防抖动时间，单位：秒
    private Integer offlineThreshold; // 设备离线阈值，单位：分钟
}
