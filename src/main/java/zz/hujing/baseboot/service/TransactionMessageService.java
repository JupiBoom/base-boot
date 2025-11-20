package zz.hujing.baseboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import zz.hujing.baseboot.domain.entity.TransactionMessage;
import zz.hujing.baseboot.core.msg.dto.TransactionMessageDTO;

/**
 * 事务消息Service
 */
public interface TransactionMessageService extends IService<TransactionMessage> {
    
    /**
     * 保存事务消息
     * @param messageDTO 消息DTO
     * @return 事务消息
     */
    TransactionMessage saveMessage(TransactionMessageDTO messageDTO);
    
    /**
     * 更新消息状态为已发送
     * @param messageId 消息ID
     * @return 是否成功
     */
    boolean updateMessageSent(String messageId);
    
    /**
     * 更新消息状态为已消费
     * @param messageId 消息ID
     * @return 是否成功
     */
    boolean updateMessageConsumed(String messageId);
    
    /**
     * 更新消息状态为失败
     * @param messageId 消息ID
     * @return 是否成功
     */
    boolean updateMessageFailed(String messageId);
    
    /**
     * 增加消息重试次数
     * @param messageId 消息ID
     * @return 是否成功
     */
    boolean incrementRetryCount(String messageId);
    
    /**
     * 根据消息ID查询消息
     * @param messageId 消息ID
     * @return 事务消息
     */
    TransactionMessage getByMessageId(String messageId);
}
