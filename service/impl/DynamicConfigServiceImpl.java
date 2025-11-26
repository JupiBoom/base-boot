package zz.hujing.baseboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.DynamicConfig;
import zz.hujing.baseboot.service.DynamicConfigService;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class DynamicConfigServiceImpl implements DynamicConfigService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_CONFIG_KEY = "dynamic:config";

    /**
     * 初始化时加载默认配置
     */
    @PostConstruct
    @Override
    public void loadDefaultConfig() {
        if (!redisTemplate.hasKey(REDIS_CONFIG_KEY)) {
            DynamicConfig defaultConfig = new DynamicConfig();
            defaultConfig.setDataCollectionFrequency(300); // 默认5分钟
            defaultConfig.setAlarmThreshold(3); // 默认连续3次异常
            defaultConfig.setAlarmCooldown(30); // 默认30秒防抖动
            defaultConfig.setOfflineThreshold(5); // 默认5分钟离线
            redisTemplate.opsForValue().set(REDIS_CONFIG_KEY, defaultConfig, 7, TimeUnit.DAYS);
        }
    }

    @Override
    public DynamicConfig getCurrentConfig() {
        return (DynamicConfig) redisTemplate.opsForValue().get(REDIS_CONFIG_KEY);
    }

    @Override
    public void updateConfig(DynamicConfig config) {
        if (config != null) {
            // 验证配置项的合理性
            if (config.getDataCollectionFrequency() == null || config.getDataCollectionFrequency() < 10) {
                config.setDataCollectionFrequency(10); // 最小10秒
            }
            if (config.getAlarmThreshold() == null || config.getAlarmThreshold() < 1) {
                config.setAlarmThreshold(1); // 最小1次
            }
            if (config.getAlarmCooldown() == null || config.getAlarmCooldown() < 5) {
                config.setAlarmCooldown(5); // 最小5秒
            }
            if (config.getOfflineThreshold() == null || config.getOfflineThreshold() < 1) {
                config.setOfflineThreshold(1); // 最小1分钟
            }
            redisTemplate.opsForValue().set(REDIS_CONFIG_KEY, config, 7, TimeUnit.DAYS);
        }
    }
}
