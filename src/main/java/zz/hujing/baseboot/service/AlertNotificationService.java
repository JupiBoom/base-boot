package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.alert.AlertRecord;

import java.util.List;

public interface AlertNotificationService {
    /**
     * 发送预警通知
     * @param alertRecord 预警记录
     */
    void sendAlertNotification(AlertRecord alertRecord);

    /**
     * 批量发送预警通知
     * @param alertRecords 预警记录列表
     */
    void sendBatchAlertNotifications(List<AlertRecord> alertRecords);

    /**
     * 发送库存健康报告
     * @param reportType 报告类型（周报/月报）
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    void sendInventoryHealthReport(String reportType, String startDate, String endDate);

    /**
     * 发送补货建议报告
     * @param productId 产品ID
     */
    void sendReplenishmentSuggestionReport(Long productId);
}