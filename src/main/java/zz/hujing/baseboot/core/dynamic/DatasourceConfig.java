package zz.hujing.baseboot.core.dynamic;

import com.google.common.collect.Maps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

/**
 * Dynamic DataSource Configuration
 **/
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@ConditionalOnProperty(prefix = "base.boot.system", name = "open-dynamic-data-source", havingValue = "true")
public class DatasourceConfig {

    private final DynamicDataSourceProperties dynamicDataSourceProperties;

    public DatasourceConfig(DynamicDataSourceProperties dynamicDataSourceProperties) {
        this.dynamicDataSourceProperties = dynamicDataSourceProperties;
    }

    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource() {
        Map<Object, Object> targetDataSource = Maps.newHashMap();
        
        // 添加默认数据源（master、slaver等）
        dynamicDataSourceProperties.getDynamic().forEach((k, v) -> 
            targetDataSource.put(DynamicDataSourceContext.DataSourceType.valueOf(k.trim().toUpperCase()), 
                v.initializeDataSourceBuilder().type(v.getType()).build()));
        
        // 添加租户数据源
        if (dynamicDataSourceProperties.getTenants() != null) {
            dynamicDataSourceProperties.getTenants().forEach((tenantKey, v) -> 
                targetDataSource.put(tenantKey, 
                    v.initializeDataSourceBuilder().type(v.getType()).build()));
        }
        
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSource);
        dynamicDataSource.setDefaultTargetDataSource(targetDataSource.get(DynamicDataSourceContext.DataSourceType.MASTER));
        return dynamicDataSource;
    }
}
