package zz.hujing.baseboot.core.tx;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * TCC模式接口定义
 */
@LocalTCC
public interface TccAction {
    
    /**
     * Try阶段方法
     * @param actionContext 事务上下文
     * @param businessKey 业务标识
     * @param params 业务参数
     * @return 是否成功
     */
    @TwoPhaseBusinessAction(name = "tccAction", commitMethod = "commit", rollbackMethod = "rollback")
    boolean tryAction(BusinessActionContext actionContext,
                     @BusinessActionContextParameter(paramName = "businessKey") String businessKey,
                     @BusinessActionContextParameter(paramName = "params") String params);
    
    /**
     * Commit阶段方法
     * @param actionContext 事务上下文
     * @return 是否成功
     */
    boolean commit(BusinessActionContext actionContext);
    
    /**
     * Rollback阶段方法
     * @param actionContext 事务上下文
     * @return 是否成功
     */
    boolean rollback(BusinessActionContext actionContext);
}