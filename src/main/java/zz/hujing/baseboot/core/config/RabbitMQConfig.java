package zz.hujing.baseboot.core.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    /**
     * 设备数据队列
     */
    public static final String DEVICE_DATA_QUEUE = "device.data.queue";

    /**
     * 设备数据交换机
     */
    public static final String DEVICE_DATA_EXCHANGE = "device.data.exchange";

    /**
     * 设备数据路由键
     */
    public static final String DEVICE_DATA_ROUTING_KEY = "device.data.routing.key";

    /**
     * 创建设备数据队列
     * @return 队列
     */
    @Bean
    public Queue deviceDataQueue() {
        return QueueBuilder.durable(DEVICE_DATA_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", DEVICE_DATA_QUEUE + ".dlq")
                .build();
    }

    /**
     * 创建设备数据交换机
     * @return 交换机
     */
    @Bean
    public DirectExchange deviceDataExchange() {
        return new DirectExchange(DEVICE_DATA_EXCHANGE, true, false);
    }

    /**
     * 绑定队列和交换机
     * @param deviceDataQueue 设备数据队列
     * @param deviceDataExchange 设备数据交换机
     * @return 绑定关系
     */
    @Bean
    public Binding deviceDataBinding(Queue deviceDataQueue, DirectExchange deviceDataExchange) {
        return BindingBuilder.bind(deviceDataQueue)
                .to(deviceDataExchange)
                .with(DEVICE_DATA_ROUTING_KEY);
    }
}