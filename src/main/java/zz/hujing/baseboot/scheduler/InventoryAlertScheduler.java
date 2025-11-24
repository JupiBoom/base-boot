package zz.hujing.baseboot.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.domain.alert.AlertRecord;
import zz.hujing.baseboot.service.InventoryMonitorService;
import zz.hujing.baseboot.service.AlertNotificationService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryAlertScheduler {

    private final InventoryMonitorService inventoryMonitorService;
    private final AlertNotificationService alertNotificationService;

    /**
     * 每天早上9点执行库存监控
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void scheduledInventoryMonitoring() {
        List<AlertRecord> alerts = inventoryMonitorService.monitorInventoryLevels();
        if (!alerts.isEmpty()) {
            alertNotificationService.sendBatchAlertNotifications(alerts);
        }
    }

    /**
     * 每周一早上10点发送库存健康周报
     */
    @Scheduled(cron = "0 0 10 * * MON")
    public void scheduledWeeklyInventoryReport() {
        alertNotificationService.sendInventoryHealthReport("周报", "周一", "周日");
    }

    /**
     * 每月1号早上10点发送库存健康月报
     */
    @Scheduled(cron = "0 0 10 1 * ?")
    public void scheduledMonthlyInventoryReport() {
        alertNotificationService.sendInventoryHealthReport("月报", "月初", "月末");
    }
}