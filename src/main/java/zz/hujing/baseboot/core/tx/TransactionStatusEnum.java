package zz.hujing.baseboot.core.tx;

/**
 * 事务状态枚举
 */
public enum TransactionStatusEnum {
    
    /**
     * 初始状态
     */
    INIT(0, "初始状态"),
    
    /**
     * 已提交
     */
    COMMITTED(1, "已提交"),
    
    /**
     * 已回滚
     */
    ROLLBACKED(2, "已回滚"),
    
    /**
     * 超时
     */
    TIMEOUT(3, "超时"),
    
    /**
     * 补偿中
     */
    COMPENSATING(4, "补偿中"),
    
    /**
     * 补偿成功
     */
    COMPENSATE_SUCCESS(5, "补偿成功"),
    
    /**
     * 补偿失败
     */
    COMPENSATE_FAILED(6, "补偿失败");
    
    private final Integer code;
    private final String message;
    
    TransactionStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public static TransactionStatusEnum fromCode(Integer code) {
        for (TransactionStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
