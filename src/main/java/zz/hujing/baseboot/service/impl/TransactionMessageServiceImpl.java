package zz.hujing.baseboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.entity.TransactionMessage;
import zz.hujing.baseboot.mapper.TransactionMessageMapper;
import zz.hujing.baseboot.service.TransactionMessageService;
import zz.hujing.baseboot.core.msg.dto.TransactionMessageDTO;

import java.time.LocalDateTime;

/**
 * 事务消息Service实现
 */
@Service
public class TransactionMessageServiceImpl extends ServiceImpl<TransactionMessageMapper, TransactionMessage> implements TransactionMessageService {
    
    @Override
    public TransactionMessage saveMessage(TransactionMessageDTO messageDTO) {
        TransactionMessage message = new TransactionMessage();
        message.setMessageId(messageDTO.getMessageId());
        message.setBusinessKey(messageDTO.getBusinessKey());
        message.setBusinessType(messageDTO.getBusinessType());
        message.setContent(messageDTO.getContent());
        message.setStatus(0); // 待发送
        message.setRetryCount(0);
        message.setMaxRetryCount(3);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        this.save(message);
        return message;
    }
    
    @Override
    public boolean updateMessageSent(String messageId) {
        TransactionMessage message = this.getByMessageId(messageId);
        if (message != null) {
            message.setStatus(1); // 已发送
            message.setSendTime(LocalDateTime.now());
            message.setUpdateTime(LocalDateTime.now());
            return this.updateById(message);
        }
        return false;
    }
    
    @Override
    public boolean updateMessageConsumed(String messageId) {
        TransactionMessage message = this.getByMessageId(messageId);
        if (message != null) {
            message.setStatus(2); // 已消费
            message.setConsumerTime(LocalDateTime.now());
            message.setUpdateTime(LocalDateTime.now());
            return this.updateById(message);
        }
        return false;
    }
    
    @Override
    public boolean updateMessageFailed(String messageId) {
        TransactionMessage message = this.getByMessageId(messageId);
        if (message != null) {
            message.setStatus(3); // 失败
            message.setUpdateTime(LocalDateTime.now());
            return this.updateById(message);
        }
        return false;
    }
    
    @Override
    public boolean incrementRetryCount(String messageId) {
        TransactionMessage message = this.getByMessageId(messageId);
        if (message != null) {
            message.setRetryCount(message.getRetryCount() + 1);
            message.setUpdateTime(LocalDateTime.now());
            return this.updateById(message);
        }
        return false;
    }
    
    @Override
    public TransactionMessage getByMessageId(String messageId) {
        return this.lambdaQuery()
                .eq(TransactionMessage::getMessageId, messageId)
                .one();
    }
}
