package zz.hujing.baseboot.core.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import zz.hujing.baseboot.core.dynamic.DynamicDataSource;
import zz.hujing.baseboot.core.tenant.mapper.TenantMapper;
import zz.hujing.baseboot.core.tenant.model.Tenant;
import zz.hujing.baseboot.core.tenant.service.TenantService;
import zz.hujing.baseboot.core.tenant.util.TenantDataSourceManager;
import zz.hujing.baseboot.core.tenant.util.TenantEncryptionUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 租户信息Service实现类
 * @author hujing
 */
@Slf4j
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    @Override
    public Tenant getByTenantKey(String tenantKey) {
        QueryWrapper<Tenant> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_key", tenantKey);
        wrapper.eq("deleted", 0);
        return baseMapper.selectOne(wrapper);
    }

    @Autowired
    private DynamicDataSource dynamicDataSource;
    
    @Autowired
    private TenantEncryptionUtil tenantEncryptionUtil;

    @Override
    public boolean registerTenant(Tenant tenant) {
        // 检查租户标识是否已存在
        Tenant existingTenant = getByTenantKey(tenant.getTenantKey());
        if (Objects.nonNull(existingTenant)) {
            return false;
        }

        // 设置默认值
        tenant.setStatus(1);
        tenant.setDeleted(0);
        tenant.setVersion(1);
        tenant.setCreateTime(LocalDateTime.now());
        tenant.setUpdateTime(LocalDateTime.now());

        // 加密数据库密码
        String encryptedPassword = tenantEncryptionUtil.encrypt(tenant.getDbPassword());
        tenant.setDbPassword(encryptedPassword);

        // 保存租户信息
        boolean saveResult = save(tenant);
        if (!saveResult) {
            return false;
        }

        // 创建租户数据库
        boolean dbResult = createTenantDatabase(tenant);
        if (!dbResult) {
            // 回滚租户信息
            removeById(tenant.getId());
            return false;
        }

        // 动态添加租户数据源到动态数据源中
        TenantDataSourceManager.addTenantDataSourceToDynamic(dynamicDataSource, tenant);

        return true;
    }

    /**
     * 创建租户数据库
     * @param tenant 租户信息
     * @return 创建结果
     */
    @Override
    public boolean createTenantDatabase(Tenant tenant) {
        // TODO: 实现租户数据库的创建逻辑
        // 这里可以使用Flyway或Liquibase等数据库迁移工具
        // 也可以直接执行SQL脚本创建数据库和表结构
        log.info("Create tenant database: {}", tenant.getDbUrl());
        return true;
    }

    /**
     * 重写获取方法，解密数据库密码
     * @param id 租户ID
     * @return 解密后的租户信息
     */
    @Override
    public Tenant getById(Serializable id) {
        Tenant tenant = super.getById(id);
        if (tenant != null) {
            tenant.setDbPassword(tenantEncryptionUtil.decrypt(tenant.getDbPassword()));
        }
        return tenant;
    }

    /**
     * 重写根据租户标识获取方法，解密数据库密码
     * @param tenantKey 租户标识
     * @return 解密后的租户信息
     */
    @Override
    public Tenant getByTenantKey(String tenantKey) {
        QueryWrapper<Tenant> wrapper = new QueryWrapper<>();
        wrapper.eq("tenant_key", tenantKey);
        wrapper.eq("deleted", 0);
        Tenant tenant = baseMapper.selectOne(wrapper);
        if (tenant != null) {
            tenant.setDbPassword(tenantEncryptionUtil.decrypt(tenant.getDbPassword()));
        }
        return tenant;
    }
}