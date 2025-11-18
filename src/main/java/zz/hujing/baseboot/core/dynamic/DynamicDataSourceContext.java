package zz.hujing.baseboot.core.dynamic;

import zz.hujing.baseboot.core.tenant.TenantContext;

/**
 * 动态数据库上下文
 **/
public class DynamicDataSourceContext {

    public static final ThreadLocal<Object> dataSourceContext = new ThreadLocal<>();

    public static void setDataSourceType(DataSourceType dataSourceType) {
        dataSourceContext.set(dataSourceType);
    }

    public static void setTenantKey(String tenantKey) {
        dataSourceContext.set(tenantKey);
    }

    public static Object get(){
        // 优先使用租户标识作为数据源键
        Object tenantKey = TenantContext.getTenantKey();
        if (tenantKey != null) {
            return tenantKey;
        }
        // 否则使用默认的数据源类型
        return dataSourceContext.get() != null ? dataSourceContext.get() : DataSourceType.MASTER;
    }

    public static void remove(){
        dataSourceContext.remove();
    }

    public static void master(){
        dataSourceContext.set(DataSourceType.MASTER);
    }

    public enum DataSourceType{
        MASTER,SLAVER
    }
}
