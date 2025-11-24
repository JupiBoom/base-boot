package zz.hujing.baseboot.iot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DEVICE_DATA_QUEUE = "device_data_queue";
    public static final String DEVICE_DATA_EXCHANGE = "device_data_exchange";
    public static final String DEVICE_DATA_ROUTING_KEY = "device_data_routing_key";

    @Bean
    public Queue deviceDataQueue() {
        return new Queue(DEVICE_DATA_QUEUE, true);
    }

    @Bean
    public TopicExchange deviceDataExchange() {
        return new TopicExchange(DEVICE_DATA_EXCHANGE);
    }

    @Bean
    public Binding deviceDataBinding(Queue deviceDataQueue, TopicExchange deviceDataExchange) {
        return BindingBuilder.bind(deviceDataQueue).to(deviceDataExchange).with(DEVICE_DATA_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}