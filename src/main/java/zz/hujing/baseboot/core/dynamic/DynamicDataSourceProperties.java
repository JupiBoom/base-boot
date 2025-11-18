package zz.hujing.baseboot.core.dynamic;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 动态数据源配置
 **/
@ConfigurationProperties("spring.datasource")
@Data
public class DynamicDataSourceProperties {

    private Map<String, DataSourceProperties> dynamic;

    // 多租户相关配置
    private Map<String, DataSourceProperties> tenants;

    // 手动添加getter方法
    public Map<String, DataSourceProperties> getTenants() {
        return tenants;
    }

    // 手动添加setter方法
    public void setTenants(Map<String, DataSourceProperties> tenants) {
        this.tenants = tenants;
    }

    // 手动添加getter方法
    public Map<String, DataSourceProperties> getDynamic() {
        return dynamic;
    }

    // 手动添加setter方法
    public void setDynamic(Map<String, DataSourceProperties> dynamic) {
        this.dynamic = dynamic;
    }

}
