package zz.hujing.baseboot.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 基本脚手架BaseBoot核心配置类
 **/
@ConfigurationProperties(prefix = "base.boot.system")
@Component
@Data
public class BaseBootProperties {

    private boolean openDynamicDataSource = false;
    
    private boolean openTenant = false;
    
    private String tenantHeader = "X-Tenant-Id";
    
    private String tenantKeyColumn = "tenant_id";
    
    private String tenantEncryptKey = "baseboot-tenant-key";
    
    // 手动添加getter和setter方法
    public boolean getOpenDynamicDataSource() {
        return openDynamicDataSource;
    }
    
    public void setOpenDynamicDataSource(boolean openDynamicDataSource) {
        this.openDynamicDataSource = openDynamicDataSource;
    }
    
    public boolean getOpenTenant() {
        return openTenant;
    }
    
    public void setOpenTenant(boolean openTenant) {
        this.openTenant = openTenant;
    }
    
    public String getTenantHeader() {
        return tenantHeader;
    }
    
    public void setTenantHeader(String tenantHeader) {
        this.tenantHeader = tenantHeader;
    }
    
    public String getTenantKeyColumn() {
        return tenantKeyColumn;
    }
    
    public void setTenantKeyColumn(String tenantKeyColumn) {
        this.tenantKeyColumn = tenantKeyColumn;
    }
    
    public String getTenantEncryptKey() {
        return tenantEncryptKey;
    }
    
    public void setTenantEncryptKey(String tenantEncryptKey) {
        this.tenantEncryptKey = tenantEncryptKey;
    }
}
