package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.AlertRecord;

import java.util.List;

/**
 * 预警通知服务接口
 */
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
     * 发送邮件通知
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendEmailNotification(String to, String subject, String content);

    /**
     * 发送短信通知
     * @param phoneNumber 手机号码
     * @param content 内容
     */
    void sendSmsNotification(String phoneNumber, String content);
}
