package zz.hujing.baseboot.core.tenant.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import zz.hujing.baseboot.core.tenant.TenantContext;
import zz.hujing.baseboot.core.tenant.util.GhostTenantHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 租户拦截器
 * @author hujing
 */
public class TenantInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TenantInterceptor.class);

    /**
     * 租户标识请求头名称
     */
    private static final String TENANT_HEADER = "X-Tenant-Key";

    @Autowired
    private GhostTenantHandler ghostTenantHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头中获取租户标识
        String tenantKey = request.getHeader(TENANT_HEADER);
        if (tenantKey != null && !tenantKey.isEmpty()) {
            // 检查租户是否存在（防止幽灵租户）
            if (!ghostTenantHandler.checkTenantExists(tenantKey)) {
                // 可以根据业务需求返回错误信息或跳转到错误页面
                log.warn("Ghost tenant detected: {}", tenantKey);
                // 这里简单处理，继续执行，但实际业务中可能需要拒绝请求
            }
            // 设置租户标识到上下文
            TenantContext.setTenantKey(tenantKey);
            log.debug("Set tenant key: {}", tenantKey);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        // 清除租户上下文
        TenantContext.clear();
        log.debug("Clear tenant context");
    }
}