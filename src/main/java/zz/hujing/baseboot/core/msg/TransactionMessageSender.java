package zz.hujing.baseboot.core.msg;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.core.msg.dto.TransactionMessageDTO;
import zz.hujing.baseboot.core.tx.LocalTransactionExecutor;
import zz.hujing.baseboot.service.TransactionMessageService;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 事务消息发送器
 */
@Component
public class TransactionMessageSender {
    
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    
    @Resource
    private TransactionMessageService transactionMessageService;
    
    /**
     * 发送事务消息
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @param businessKey 业务标识
     * @param executor 本地事务执行器
     * @return 消息ID
     */
    public String sendTransactionMessage(String topic, String tag, TransactionMessageDTO message, 
                                          String businessKey, LocalTransactionExecutor<?, ?> executor) {
        String destination = topic + ":" + tag;
        String messageId = UUID.randomUUID().toString();
        
        message.setMessageId(messageId);
        message.setBusinessKey(businessKey);
        
        // 保存消息到数据库
        transactionMessageService.saveMessage(message);
        
        // 构建消息
        Message<TransactionMessageDTO> rocketMessage = MessageBuilder
                .withPayload(message)
                .setHeader(RocketMQHeaders.TRANSACTION_ID, messageId)
                .setHeader(RocketMQHeaders.KEYS, businessKey)
                .build();
        
        // 发送事务消息
        rocketMQTemplate.sendMessageInTransaction(destination, rocketMessage, executor);
        
        return messageId;
    }
}