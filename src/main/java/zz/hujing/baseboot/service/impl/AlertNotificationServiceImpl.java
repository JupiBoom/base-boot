package zz.hujing.baseboot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.AlertRecord;
import zz.hujing.baseboot.domain.Product;
import zz.hujing.baseboot.repository.ProductRepository;
import zz.hujing.baseboot.service.AlertNotificationService;

import java.util.List;

/**
 * 预警通知服务实现类
 */
@Slf4j
@Service
public class AlertNotificationServiceImpl implements AlertNotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ProductRepository productRepository;

    /**
     * 发送预警通知
     * @param alertRecord 预警记录
     */
    @Override
    public void sendAlertNotification(AlertRecord alertRecord) {
        if (alertRecord == null) {
            log.error("预警记录为空，无法发送通知");
            return;
        }

        Product product = productRepository.findById(alertRecord.getProductId()).orElse(null);
        if (product == null) {
            log.error("产品不存在，无法发送预警通知，productId: {}", alertRecord.getProductId());
            return;
        }

        // 构建通知内容
        String subject = "库存预警通知";
        String content = buildAlertContent(alertRecord, product);

        // 发送邮件通知（这里可以配置实际的收件人邮箱）
        sendEmailNotification("admin@example.com", subject, content);

        // 发送短信通知（这里可以集成短信服务API）
        // sendSmsNotification("13800138000", content);
    }

    /**
     * 批量发送预警通知
     * @param alertRecords 预警记录列表
     */
    @Override
    public void sendBatchAlertNotifications(List<AlertRecord> alertRecords) {
        if (alertRecords == null || alertRecords.isEmpty()) {
            log.warn("预警记录列表为空，无需发送通知");
            return;
        }

        for (AlertRecord alertRecord : alertRecords) {
            try {
                sendAlertNotification(alertRecord);
            } catch (Exception e) {
                log.error("发送预警通知失败，alertRecordId: {}", alertRecord.getId(), e);
            }
        }
    }

    /**
     * 发送邮件通知
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendEmailNotification(String to, String subject, String content) {
        if (to == null || to.isEmpty()) {
            log.error("收件人邮箱为空，无法发送邮件");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            message.setFrom("inventory-alert@example.com"); // 配置发件人邮箱

            mailSender.send(message);
            log.info("邮件通知发送成功，收件人: {}", to);
        } catch (Exception e) {
            log.error("发送邮件通知失败，收件人: {}", to, e);
        }
    }

    /**
     * 发送短信通知
     * @param phoneNumber 手机号码
     * @param content 内容
     */
    @Override
    public void sendSmsNotification(String phoneNumber, String content) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            log.error("手机号码为空，无法发送短信");
            return;
        }

        try {
            // 这里可以集成短信服务API，比如阿里云短信、腾讯云短信等
            // 示例代码：
            // SmsClient smsClient = new SmsClient();
            // smsClient.sendSms(phoneNumber, content);
            log.info("短信通知发送成功，手机号码: {}, 内容: {}", phoneNumber, content);
        } catch (Exception e) {
            log.error("发送短信通知失败，手机号码: {}", phoneNumber, e);
        }
    }

    /**
     * 构建预警通知内容
     * @param alertRecord 预警记录
     * @param product 产品信息
     * @return 通知内容
     */
    private String buildAlertContent(AlertRecord alertRecord, Product product) {
        StringBuilder content = new StringBuilder();
        content.append("尊敬的管理员：\n");
        content.append("您好！\n");
        content.append("系统检测到库存异常情况，具体信息如下：\n");
        content.append(String.format("产品名称：%s\n", product.getProductName()));
        content.append(String.format("产品编码：%s\n", product.getProductCode()));
        content.append(String.format("预警类型：%s\n", getAlertTypeName(alertRecord.getAlertType())));
        content.append(String.format("预警级别：%s\n", getAlertLevelName(alertRecord.getAlertLevel())));
        content.append(String.format("预警内容：%s\n", alertRecord.getAlertContent()));
        content.append(String.format("预警时间：%s\n", alertRecord.getCreateTime()));
        content.append("请及时处理！\n");
        content.append("库存预警系统\n");
        content.append(String.format("%s", alertRecord.getCreateTime()));
        return content.toString();
    }

    /**
     * 获取预警类型名称
     * @param alertType 预警类型
     * @return 预警类型名称
     */
    private String getAlertTypeName(Integer alertType) {
        switch (alertType) {
            case 1:
                return "库存不足";
            case 2:
                return "库存周转率异常";
            case 3:
                return "呆滞库存";
            default:
                return "未知类型";
        }
    }

    /**
     * 获取预警级别名称
     * @param alertLevel 预警级别
     * @return 预警级别名称
     */
    private String getAlertLevelName(Integer alertLevel) {
        switch (alertLevel) {
            case 1:
                return "提醒";
            case 2:
                return "警告";
            case 3:
                return "紧急";
            default:
                return "未知级别";
        }
    }
}
