package zz.hujing.baseboot.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 事务消息实体
 */
@Data
@TableName("transaction_message")
public class TransactionMessage {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 消息ID
     */
    private String messageId;
    
    /**
     * 业务标识
     */
    private String businessKey;
    
    /**
     * 业务类型
     */
    private String businessType;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 状态：0-待发送 1-已发送 2-已消费 3-失败
     */
    private Integer status;
    
    /**
     * 重试次数
     */
    private Integer retryCount;
    
    /**
     * 最大重试次数
     */
    private Integer maxRetryCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
    
    /**
     * 消费时间
     */
    private LocalDateTime consumerTime;
    
    /**
     * 备注
     */
    private String remark;
}
