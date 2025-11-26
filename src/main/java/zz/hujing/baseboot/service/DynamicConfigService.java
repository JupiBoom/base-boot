package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.DynamicConfig;

public interface DynamicConfigService {
    /**
     * 获取当前配置
     * @return 动态配置对象
     */
    DynamicConfig getCurrentConfig();

    /**
     * 更新配置
     * @param config 新的配置对象
     */
    void updateConfig(DynamicConfig config);

    /**
     * 加载默认配置
     */
    void loadDefaultConfig();
}
