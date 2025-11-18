package zz.hujing.baseboot.core.tenant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import zz.hujing.baseboot.core.tenant.model.Tenant;

/**
 * 租户信息Service接口
 * @author hujing
 */
public interface TenantService extends IService<Tenant> {

    /**
     * 根据租户标识获取租户信息
     * @param tenantKey 租户标识
     * @return 租户信息
     */
    Tenant getByTenantKey(String tenantKey);

    /**
     * 注册新租户
     * @param tenant 租户信息
     * @return 注册结果
     */
    boolean registerTenant(Tenant tenant);

    /**
     * 创建租户数据库
     * @param tenant 租户信息
     * @return 创建结果
     */
    boolean createTenantDatabase(Tenant tenant);
}