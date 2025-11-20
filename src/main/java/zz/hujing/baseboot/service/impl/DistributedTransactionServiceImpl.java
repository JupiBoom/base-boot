package zz.hujing.baseboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.entity.DistributedTransaction;
import zz.hujing.baseboot.mapper.DistributedTransactionMapper;
import zz.hujing.baseboot.service.DistributedTransactionService;
import zz.hujing.baseboot.core.tx.TransactionStatusEnum;

import java.time.LocalDateTime;

/**
 * 分布式事务Service实现
 */
@Service
public class DistributedTransactionServiceImpl extends ServiceImpl<DistributedTransactionMapper, DistributedTransaction> implements DistributedTransactionService {
    
    @Override
    public DistributedTransaction createTransaction(String transactionId, String businessKey) {
        DistributedTransaction transaction = new DistributedTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setBusinessKey(businessKey);
        transaction.setStatus(0); // 初始状态
        transaction.setCreateTime(LocalDateTime.now());
        transaction.setUpdateTime(LocalDateTime.now());
        
        this.save(transaction);
        return transaction;
    }
    
    @Override
    public boolean updateTransactionStatus(String transactionId, TransactionStatusEnum status) {
        DistributedTransaction transaction = new DistributedTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setStatus(status.getCode());
        transaction.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(transaction);
    }
    
    @Override
    public DistributedTransaction getByTransactionId(String transactionId) {
        return this.lambdaQuery()
                .eq(DistributedTransaction::getTransactionId, transactionId)
                .one();
    }
    
    @Override
    public DistributedTransaction getByBusinessKey(String businessKey) {
        return this.lambdaQuery()
                .eq(DistributedTransaction::getBusinessKey, businessKey)
                .one();
    }
    
    @Override
    public TransactionStatusEnum getTransactionStatus(String transactionId) {
        DistributedTransaction transaction = getByTransactionId(transactionId);
        return transaction != null ? TransactionStatusEnum.fromCode(transaction.getStatus()) : null;
    }
    
    @Override
    public TransactionStatusEnum getBusinessTransactionStatus(String businessKey) {
        DistributedTransaction transaction = getByBusinessKey(businessKey);
        return transaction != null ? TransactionStatusEnum.fromCode(transaction.getStatus()) : null;
    }
}
