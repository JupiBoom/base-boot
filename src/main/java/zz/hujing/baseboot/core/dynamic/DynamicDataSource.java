package zz.hujing.baseboot.core.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.core.tenant.TenantContext;
import zz.hujing.baseboot.core.tenant.monitor.TenantDataSourceMonitor;
import zz.hujing.baseboot.core.tenant.service.TenantService;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.time.Instant;

/**
 * 动态数据源（支持多租户）
 * @author hujing
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private TenantService tenantService;

    @Resource
    private TenantDataSourceMonitor tenantDataSourceMonitor;

    public void setTenantService(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        // 开始监控
        Instant startTime = tenantDataSourceMonitor.startSwitch();
        
        Object lookupKey = DynamicDataSourceContext.get();
        
        // 结束监控
        String tenantKey = TenantContext.getTenantKey();
        tenantDataSourceMonitor.endSwitch(startTime, tenantKey, lookupKey);
        
        return lookupKey;
    }

    public Map<Object, Object> getTargetDataSources() {
        try {
            Field field = AbstractRoutingDataSource.class.getDeclaredField("targetDataSources");
            field.setAccessible(true);
            return (Map<Object, Object>) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get target data sources", e);
        }
    }
}
