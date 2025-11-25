package zz.hujing.baseboot.core.config;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableScheduling
@RefreshScope
public class MQTTConfig {
    @Value("${mqtt.broker:tcp://localhost:1883}")
    private String broker;

    @Value("${mqtt.clientId:iot-data-collector}")
    private String clientId;

    @Value("${mqtt.username:}")
    private String username;

    @Value("${mqtt.password:}")
    private String password;

    @Value("${mqtt.topic:device/#}")
    private String topic;

    @Value("${mqtt.qos:1}")
    private int qos;

    @Value("${mqtt.keepAliveInterval:60}")
    private int keepAliveInterval;

    private MqttClient mqttClient;

    private final RabbitTemplate rabbitTemplate;

    public MQTTConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 初始化MQTT客户端
     */
    @PostConstruct
    public void init() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        mqttClient = new MqttClient(broker, clientId, persistence);

        MqttConnectOptions connOpts = new MqttConnectOptions();
        if (username != null && !username.isEmpty()) {
            connOpts.setUserName(username);
        }
        if (password != null && !password.isEmpty()) {
            connOpts.setPassword(password.toCharArray());
        }
        connOpts.setCleanSession(true);
        connOpts.setAutomaticReconnect(true);
        connOpts.setConnectionTimeout(10);
        connOpts.setKeepAliveInterval(keepAliveInterval);

        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("MQTT connection lost: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                System.out.println("MQTT message arrived: topic=" + topic + ", payload=" + payload);
                // 将MQTT消息转发到RabbitMQ
                rabbitTemplate.convertAndSend(RabbitMQConfig.DEVICE_DATA_EXCHANGE,
                        RabbitMQConfig.DEVICE_DATA_ROUTING_KEY, payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // 消息发送完成回调
            }
        });

        mqttClient.connect(connOpts);
        mqttClient.subscribe(topic, qos);
        System.out.println("MQTT client connected to broker: " + broker);
    }

    /**
     * 销毁MQTT客户端
     */
    @PreDestroy
    public void destroy() throws MqttException {
        if (mqttClient != null && mqttClient.isConnected()) {
            mqttClient.disconnect();
            mqttClient.close();
            System.out.println("MQTT client disconnected");
        }
    }
}