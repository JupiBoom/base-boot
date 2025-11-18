package zz.hujing.baseboot.core.tenant.util;

import org.springframework.boot.jdbc.DataSourceBuilder;
import zz.hujing.baseboot.core.dynamic.DynamicDataSource;
import org.springframework.util.Assert;
import zz.hujing.baseboot.core.dynamic.DynamicDataSource;
import zz.hujing.baseboot.core.tenant.model.Tenant;
import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 租户数据源管理器
 * 实现数据源的动态扩容与收缩
 * @author hujing
 */
public class TenantDataSourceManager {

    private static final Map<String, DataSource> TENANT_DATA_SOURCES = new ConcurrentHashMap<>();

    /**
     * 添加租户数据源
     * @param tenant 租户信息
     * @return 数据源
     */
    public static DataSource addTenantDataSource(Tenant tenant) {
        Assert.notNull(tenant, "Tenant must not be null");
        Assert.notNull(tenant.getTenantKey(), "TenantKey must not be null");

        String tenantKey = tenant.getTenantKey();
        if (TENANT_DATA_SOURCES.containsKey(tenantKey)) {
            return TENANT_DATA_SOURCES.get(tenantKey);
        }

        // 创建数据源
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(tenant.getDbUrl())
                .username(tenant.getDbUsername())
                .password(tenant.getDbPassword())
                .build();

        // 添加到数据源映射
        TENANT_DATA_SOURCES.put(tenantKey, dataSource);

        return dataSource;
    }

    /**
     * 移除租户数据源
     * @param tenantKey 租户标识
     */
    public static void removeTenantDataSource(String tenantKey) {
        Assert.notNull(tenantKey, "TenantKey must not be null");
        TENANT_DATA_SOURCES.remove(tenantKey);
    }

    /**
     * 获取租户数据源
     * @param tenantKey 租户标识
     * @return 数据源
     */
    public static DataSource getTenantDataSource(String tenantKey) {
        Assert.notNull(tenantKey, "TenantKey must not be null");
        return TENANT_DATA_SOURCES.get(tenantKey);
    }

    /**
     * 检查租户数据源是否存在
     * @param tenantKey 租户标识
     * @return 是否存在
     */
    public static boolean existsTenantDataSource(String tenantKey) {
        Assert.notNull(tenantKey, "TenantKey must not be null");
        return TENANT_DATA_SOURCES.containsKey(tenantKey);
    }

    /**
     * 将租户数据源添加到动态数据源中
     * @param dynamicDataSource 动态数据源
     * @param tenant 租户信息
     */
    public static void addTenantDataSourceToDynamic(DynamicDataSource dynamicDataSource, Tenant tenant) {
        DataSource dataSource = addTenantDataSource(tenant);
        Map<Object, Object> targetDataSources = dynamicDataSource.getTargetDataSources();
        if (targetDataSources != null) {
            targetDataSources.put(tenant.getTenantKey(), dataSource);
            // 刷新动态数据源
            dynamicDataSource.afterPropertiesSet();
        }
    }

    /**
     * 从动态数据源中移除租户数据源
     * @param dynamicDataSource 动态数据源
     * @param tenantKey 租户标识
     */
    public static void removeTenantDataSourceFromDynamic(DynamicDataSource dynamicDataSource, String tenantKey) {
        Map<Object, Object> targetDataSources = dynamicDataSource.getTargetDataSources();
        if (targetDataSources != null) {
            targetDataSources.remove(tenantKey);
            // 刷新动态数据源
            dynamicDataSource.afterPropertiesSet();
        }
        removeTenantDataSource(tenantKey);
    }
}