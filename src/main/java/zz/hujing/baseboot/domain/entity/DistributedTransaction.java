package zz.hujing.baseboot.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import zz.hujing.baseboot.core.tx.TransactionStatusEnum;

import java.time.LocalDateTime;

/**
 * 分布式事务实体
 */
@Data
@TableName("distributed_transaction")
public class DistributedTransaction {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 事务ID
     */
    private String transactionId;
    
    /**
     * 业务标识
     */
    private String businessKey;
    
    /**
     * 状态：0-初始 1-已提交 2-已回滚 3-超时 4-补偿中 5-补偿成功 6-补偿失败
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 设置事务状态
     */
    public void setTransactionStatus(TransactionStatusEnum status) {
        this.status = status.getCode();
    }
    
    /**
     * 获取事务状态
     */
    public TransactionStatusEnum getTransactionStatus() {
        return TransactionStatusEnum.fromCode(status);
    }
}
