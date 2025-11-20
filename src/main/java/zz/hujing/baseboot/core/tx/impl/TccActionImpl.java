package zz.hujing.baseboot.core.tx.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.core.tx.TccAction;

/**
 * TCC模式实现类
 */
@Component
public class TccActionImpl implements TccAction {
    
    /**
     * Try阶段：预留资源
     */
    @Override
    public boolean tryAction(BusinessActionContext actionContext, String businessKey, String params) {
        // TODO: 实现预留资源逻辑
        System.out.println("TCC Try: " + businessKey + ", params: " + params);
        return true;
    }
    
    /**
     * Commit阶段：确认资源
     */
    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String businessKey = actionContext.getActionContext("businessKey").toString();
        // TODO: 实现确认资源逻辑
        System.out.println("TCC Commit: " + businessKey);
        return true;
    }
    
    /**
     * Rollback阶段：释放资源
     */
    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String businessKey = actionContext.getActionContext("businessKey").toString();
        // TODO: 实现释放资源逻辑
        System.out.println("TCC Rollback: " + businessKey);
        return true;
    }
}