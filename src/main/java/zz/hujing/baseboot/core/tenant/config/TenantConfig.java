package zz.hujing.baseboot.core.tenant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zz.hujing.baseboot.core.tenant.interceptor.TenantInterceptor;

/**
 * 租户配置类
 * @author hujing
 */
@Configuration
public class TenantConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册租户拦截器
        registry.addInterceptor(new TenantInterceptor())
                // 排除租户注册接口
                .excludePathPatterns("/api/tenant/register")
                .addPathPatterns("/**");
    }
}