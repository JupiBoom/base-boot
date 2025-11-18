package zz.hujing.baseboot.core.tenant;

/**
 * 租户上下文
 * @author hujing
 */
public class TenantContext {

    /**
     * 线程本地存储租户标识
     */
    private static final ThreadLocal<String> TENANT_CONTEXT = new ThreadLocal<>();

    /**
     * 设置租户标识
     * @param tenantKey 租户标识
     */
    public static void setTenantKey(String tenantKey) {
        TENANT_CONTEXT.set(tenantKey);
    }

    /**
     * 获取当前租户标识
     * @return 租户标识
     */
    public static String getTenantKey() {
        return TENANT_CONTEXT.get();
    }

    /**
     * 清除租户标识
     */
    public static void clear() {
        TENANT_CONTEXT.remove();
    }
}