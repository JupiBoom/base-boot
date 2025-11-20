package zz.hujing.baseboot.core.compensate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.domain.entity.DistributedTransaction;
import zz.hujing.baseboot.service.DistributedTransactionService;
import zz.hujing.baseboot.service.TransactionMessageService;
import zz.hujing.baseboot.core.tx.TransactionStatusEnum;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 事务补偿器
 * 用于处理超时事务和异常情况
 */
@Component
public class TransactionCompensator {
    
    @Resource
    private DistributedTransactionService distributedTransactionService;
    
    @Resource
    private TransactionMessageService transactionMessageService;
    
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    
    /**
     * 定时检查超时事务
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkTimeoutTransactions() {
        // 查询超时的分布式事务
        QueryWrapper<DistributedTransaction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", TransactionStatusEnum.TIMEOUT.getCode());
        List<DistributedTransaction> timeoutTransactions = distributedTransactionService.list(queryWrapper);
        
        // 执行补偿逻辑
        for (DistributedTransaction transaction : timeoutTransactions) {
            compensateTransaction(transaction.getTransactionId());
        }
    }
    
    /**
     * 执行事务补偿
     * @param transactionId 事务ID
     */
    public void compensateTransaction(String transactionId) {
        try {
            // 获取事务信息
            DistributedTransaction transaction = distributedTransactionService.getByTransactionId(transactionId);
            if (transaction == null) {
                return;
            }
            
            // 执行补偿逻辑
            // 这里可以根据业务类型执行不同的补偿策略
            // 例如：查询业务状态并根据状态决定是重试还是回滚
            
            // 更新事务状态为补偿中
            distributedTransactionService.updateTransactionStatus(transactionId, TransactionStatusEnum.COMPENSATING);
            
            // TODO: 实现具体的补偿逻辑
            
            // 补偿完成后更新状态为补偿成功
            distributedTransactionService.updateTransactionStatus(transactionId, TransactionStatusEnum.COMPENSATE_SUCCESS);
        } catch (Exception e) {
            // 补偿失败，更新状态为补偿失败
            distributedTransactionService.updateTransactionStatus(transactionId, TransactionStatusEnum.COMPENSATE_FAILED);
            e.printStackTrace();
        }
    }
}
