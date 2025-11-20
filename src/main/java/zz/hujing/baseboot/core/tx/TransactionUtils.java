package zz.hujing.baseboot.core.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.service.DistributedTransactionService;

/**
 * 事务工具类
 */
@Component
public class TransactionUtils {
    
    @Autowired
    private DistributedTransactionService transactionService;
    
    /**
     * 创建新事务
     * @param businessKey 业务标识
     * @return 事务ID
     */
    public String createTransaction(String businessKey) {
        String transactionId = java.util.UUID.randomUUID().toString().replace("-", "");
        transactionService.createTransaction(transactionId, businessKey);
        return transactionId;
    }
    
    /**
     * 提交事务
     * @param transactionId 事务ID
     */
    public void commitTransaction(String transactionId) {
        transactionService.updateTransactionStatus(transactionId, TransactionStatusEnum.COMMITTED);
    }
    
    /**
     * 回滚事务
     * @param transactionId 事务ID
     */
    public void rollbackTransaction(String transactionId) {
        transactionService.updateTransactionStatus(transactionId, TransactionStatusEnum.ROLLBACKED);
    }
    
    /**
     * 检查事务状态
     * @param transactionId 事务ID
     * @return 事务状态
     */
    public TransactionStatusEnum checkTransactionStatus(String transactionId) {
        return transactionService.getTransactionStatus(transactionId);
    }
    
    /**
     * 检查业务事务状态
     * @param businessKey 业务标识
     * @return 事务状态
     */
    public TransactionStatusEnum checkBusinessTransactionStatus(String businessKey) {
        return transactionService.getBusinessTransactionStatus(businessKey);
    }
}
