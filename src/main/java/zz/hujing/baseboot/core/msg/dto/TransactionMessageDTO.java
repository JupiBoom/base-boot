package zz.hujing.baseboot.core.msg.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 事务消息DTO
 */
@Data
public class TransactionMessageDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
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
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 重试次数
     */
    private Integer retryCount;
    
    /**
     * 状态：0-待发送 1-已发送 2-已消费 3-失败
     */
    private Integer status;
}