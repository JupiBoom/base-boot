package zz.hujing.baseboot.core.tx;

/**
 * 本地事务执行器接口
 * 用于在RocketMQ事务消息的上下文中执行本地业务逻辑
 * @param <T> 业务参数类型
 * @param <R> 业务返回结果类型
 */
public interface LocalTransactionExecutor<T, R> {
    
    /**
     * 执行本地事务逻辑
     * @param params 业务参数
     * @return 执行结果
     */
    R executeLocalTransaction(T params);
    
    /**
     * 检查本地事务状态
     * @param params 业务参数
     * @return 事务状态：0-未知 1-提交 2-回滚
     */
    Integer checkLocalTransaction(T params);
}
