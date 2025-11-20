package zz.hujing.baseboot.core.msg;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.core.tx.LocalTransactionExecutor;
import zz.hujing.baseboot.core.msg.dto.TransactionMessageDTO;
import zz.hujing.baseboot.service.TransactionMessageService;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * RocketMQ事务监听器
 * 实现本地事务与消息发送的原子性绑定
 */
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate")
public class TransactionMsgListener implements RocketMQLocalTransactionListener {
    
    @Resource
    private TransactionMessageService transactionMessageService;
    
    /**
     * 执行本地事务
     * @param msg 消息
     * @param arg 本地事务执行器
     * @return 事务状态
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message<TransactionMessageDTO> msg, Object arg) {
        if (arg == null || !(arg instanceof LocalTransactionExecutor)) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        try {
            // 执行本地事务逻辑
            LocalTransactionExecutor<?, ?> executor = (LocalTransactionExecutor<?, ?>) arg;
            Object result = executor.executeLocalTransaction(msg.getPayload());
            
            if (result != null && (boolean) result) {
                // 本地事务执行成功，提交消息
                return RocketMQLocalTransactionState.COMMIT;
            } else {
                // 本地事务执行失败，回滚消息
                return RocketMQLocalTransactionState.ROLLBACK;
            }
        } catch (Exception e) {
            // 发生异常，回滚消息
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }
    
    /**
     * 检查本地事务状态
     * @param msg 消息
     * @return 事务状态
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        try {
            // 获取消息ID
            String messageId = Objects.requireNonNull(msg.getHeaders().get("TRANSACTION_ID")).toString();
            
            // 查询消息状态
            zz.hujing.baseboot.domain.entity.TransactionMessage message = transactionMessageService.getByMessageId(messageId);
            
            if (message == null) {
                // 消息不存在，回滚
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            
            // 根据消息状态判断事务状态
            switch (message.getStatus()) {
                case 0: // 待发送
                    // 检查本地事务是否完成
                    return RocketMQLocalTransactionState.UNKNOWN;
                case 1: // 已发送
                    // 本地事务已完成，提交消息
                    return RocketMQLocalTransactionState.COMMIT;
                case 2: // 已消费
                    // 消息已消费，提交
                    return RocketMQLocalTransactionState.COMMIT;
                default: // 其他状态，回滚
                    return RocketMQLocalTransactionState.ROLLBACK;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.UNKNOWN;
        }
    }
}
