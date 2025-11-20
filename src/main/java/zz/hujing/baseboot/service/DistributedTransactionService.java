package zz.hujing.baseboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import zz.hujing.baseboot.domain.entity.DistributedTransaction;
import zz.hujing.baseboot.core.tx.TransactionStatusEnum;

/**
 * 分布式事务Service
 */
public interface DistributedTransactionService extends IService<DistributedTransaction> {
    
    /**
     * 创建分布式事务
     * @param transactionId 事务ID
     * @param businessKey 业务标识
     * @return 分布式事务
     */
    DistributedTransaction createTransaction(String transactionId, String businessKey);
    
    /**
     * 更新事务状态
     * @param transactionId 事务ID
     * @param status 事务状态
     * @return 是否成功
     */
    boolean updateTransactionStatus(String transactionId, TransactionStatusEnum status);
    
    /**
     * 根据事务ID查询事务
     * @param transactionId 事务ID
     * @return 分布式事务
     */
    DistributedTransaction getByTransactionId(String transactionId);
    
    /**
     * 根据业务标识查询事务
     * @param businessKey 业务标识
     * @return 分布式事务
     */
    DistributedTransaction getByBusinessKey(String businessKey);
    
    /**
     * 获取事务状态
     * @param transactionId 事务ID
     * @return 事务状态枚举
     */
    TransactionStatusEnum getTransactionStatus(String transactionId);
    
    /**
     * 根据业务标识获取事务状态
     * @param businessKey 业务标识
     * @return 事务状态枚举
     */
    TransactionStatusEnum getBusinessTransactionStatus(String businessKey);
}
