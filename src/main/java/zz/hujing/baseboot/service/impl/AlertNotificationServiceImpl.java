package zz.hujing.baseboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.alert.AlertRecord;
import zz.hujing.baseboot.domain.product.Product;
import zz.hujing.baseboot.repository.ProductRepository;
import zz.hujing.baseboot.service.AlertNotificationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertNotificationServiceImpl implements AlertNotificationService {

    private final JavaMailSender mailSender;
    private final ProductRepository productRepository;

    @Override
    public void sendAlertNotification(AlertRecord alertRecord) {
        Product product = productRepository.findById(alertRecord.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found for alert: " + alertRecord.getProductId()));

        // 发送邮件通知
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("inventory-alert@example.com");
        message.setTo("admin@example.com"); // 实际应用中应该从配置或数据库中获取收件人
        message.setSubject(String.format("库存预警：%s - %s", alertRecord.getAlertLevel(), product.getProductName()));
        message.setText(alertRecord.getAlertMessage());

        mailSender.send(message);

        // 这里可以添加短信通知逻辑
    }

    @Override
    public void sendBatchAlertNotifications(List<AlertRecord> alertRecords) {
        for (AlertRecord alertRecord : alertRecords) {
            sendAlertNotification(alertRecord);
        }
    }

    @Override
    public void sendInventoryHealthReport(String reportType, String startDate, String endDate) {
        // 简化实现：发送库存健康报告邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("inventory-report@example.com");
        message.setTo("admin@example.com");
        message.setSubject(String.format("库存健康报告 - %s (%s 至 %s)", reportType, startDate, endDate));
        message.setText("库存健康报告内容..."); // 实际应用中应该生成详细的报告内容

        mailSender.send(message);
    }

    @Override
    public void sendReplenishmentSuggestionReport(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        // 简化实现：发送补货建议报告邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("inventory-report@example.com");
        message.setTo("admin@example.com");
        message.setSubject(String.format("补货建议报告 - %s", product.getProductName()));
        message.setText("补货建议报告内容..."); // 实际应用中应该生成详细的报告内容

        mailSender.send(message);
    }
}