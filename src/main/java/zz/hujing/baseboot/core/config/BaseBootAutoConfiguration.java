package zz.hujing.baseboot.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zz.hujing.baseboot.core.tenant.interceptor.TenantInterceptor;
import zz.hujing.baseboot.core.tenant.util.TenantEncryptionUtil;

/**
 * BaseBoot自动配置类
 **/
@Configuration
@EnableConfigurationProperties(BaseBootProperties.class)
public class BaseBootAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private BaseBootProperties baseBootProperties;

    /**
     * 租户拦截器
     */
    @Bean
    @ConditionalOnProperty(name = "base.boot.system.open-tenant", havingValue = "true")
    public TenantInterceptor tenantInterceptor() {
        return new TenantInterceptor();
    }

    /**
     * 租户加密工具
     */
    @Bean
    @ConditionalOnProperty(name = "base.boot.system.open-tenant", havingValue = "true")
    public TenantEncryptionUtil tenantEncryptionUtil() {
        TenantEncryptionUtil encryptionUtil = new TenantEncryptionUtil();
        encryptionUtil.setEncryptKey(baseBootProperties.getTenantEncryptKey());
        return encryptionUtil;
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (baseBootProperties.isOpenTenant()) {
            registry.addInterceptor(tenantInterceptor())
                    .addPathPatterns("/**")
                    .excludePathPatterns("/error");
        }
    }
}
