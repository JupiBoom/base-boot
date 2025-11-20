package zz.hujing.baseboot.config;

import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ配置类
 * 用于配置RocketMQ的生产者、消费者和事务管理器
 */
@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
public class RocketMQConfig {
    
    @Bean
    public RocketMQTemplate rocketMQTemplate() {
        // 配置RocketMQ模板
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        // 可以在这里添加自定义配置
        return rocketMQTemplate;
    }
}
