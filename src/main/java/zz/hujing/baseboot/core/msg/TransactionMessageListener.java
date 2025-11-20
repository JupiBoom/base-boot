package zz.hujing.baseboot.core.msg;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.core.msg.dto.TransactionMessageDTO;

/**
 * 事务消息监听器
 */
@Component
@RocketMQMessageListener(topic = "transaction-topic", consumerGroup = "base-boot-consumer-group")
public class TransactionMessageListener implements RocketMQListener<TransactionMessageDTO> {
    
    @Override
    public void onMessage(TransactionMessageDTO message) {
        // TODO: 实现消息消费逻辑
        // 1. 幂等性检查
        // 2. 业务逻辑处理
        // 3. 更新消息状态
        System.out.println("Received transaction message: " + message.getMessageId() + ", businessKey: " + message.getBusinessKey());
    }
}
