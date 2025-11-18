package zz.hujing.baseboot.core.tenant.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint.MetricDescriptor;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 租户数据源监控
 * 记录数据源切换的性能指标
 * @author hujing
 */
@Component
public class TenantDataSourceMonitor {

    private static final Logger log = LoggerFactory.getLogger(TenantDataSourceMonitor.class);

    /**
     * 数据源切换总次数
     */
    private final AtomicLong switchCount = new AtomicLong(0);

    /**
     * 数据源切换总耗时（毫秒）
     */
    private final AtomicLong totalSwitchTime = new AtomicLong(0);

    /**
     * 开始数据源切换监控
     * @return 开始时间
     */
    public Instant startSwitch() {
        return Instant.now();
    }

    /**
     * 结束数据源切换监控
     * @param startTime 开始时间
     * @param tenantKey 租户标识
     * @param dataSourceKey 数据源键
     */
    public void endSwitch(Instant startTime, String tenantKey, Object dataSourceKey) {
        Duration duration = Duration.between(startTime, Instant.now());
        long switchTime = duration.toMillis();
        
        // 更新指标
        switchCount.incrementAndGet();
        totalSwitchTime.addAndGet(switchTime);
        
        // 记录日志
        log.debug("DataSource switched for tenant: {}, key: {}, time: {}ms", tenantKey, dataSourceKey, switchTime);
        
        // TODO: 可以将指标发布到监控系统（如Prometheus）
    }

    /**
     * 获取数据源切换总次数
     * @return 总次数
     */
    public long getSwitchCount() {
        return switchCount.get();
    }

    /**
     * 获取数据源切换总耗时
     * @return 总耗时（毫秒）
     */
    public long getTotalSwitchTime() {
        return totalSwitchTime.get();
    }

    /**
     * 获取平均切换时间
     * @return 平均切换时间（毫秒）
     */
    public double getAverageSwitchTime() {
        long count = switchCount.get();
        return count == 0 ? 0 : totalSwitchTime.get() / (double) count;
    }
}
