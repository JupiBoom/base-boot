package zz.hujing.baseboot.core.tenant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zz.hujing.baseboot.core.tenant.interceptor.TenantSqlInterceptor;

/**
 * 租户MyBatis配置
 * 注册租户SQL拦截器
 * @author hujing
 */
@Configuration
public class TenantMyBatisConfig {

    @Bean
    public TenantSqlInterceptor tenantSqlInterceptor() {
        return new TenantSqlInterceptor();
    }
}
