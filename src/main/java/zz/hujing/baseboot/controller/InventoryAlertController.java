package zz.hujing.baseboot.controller;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.domain.AlertRecord;
import zz.hujing.baseboot.domain.Product;
import zz.hujing.baseboot.service.AlertNotificationService;
import zz.hujing.baseboot.service.InventoryMonitorService;
import zz.hujing.baseboot.service.ReportService;
import zz.hujing.baseboot.core.result.CommonResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 库存预警系统控制器
 */
@RestController
@RequestMapping("/inventory-alert")
public class InventoryAlertController {

    @Autowired
    private InventoryMonitorService inventoryMonitorService;

    @Autowired
    private AlertNotificationService alertNotificationService;

    @Autowired
    private ReportService reportService;

    /**
     * 监控所有产品的库存水位
     * @return 预警记录列表
     */
    @PostMapping("/monitor/inventory-level")
    public CommonResult<List<AlertRecord>> monitorInventoryLevel() {
        List<AlertRecord> alertRecords = inventoryMonitorService.monitorInventoryLevel();
        // 发送预警通知
        if (!alertRecords.isEmpty()) {
            alertNotificationService.sendBatchAlertNotifications(alertRecords);
        }
        return CommonResult.success(alertRecords);
    }

    /**
     * 监控单个产品的库存水位
     * @param productId 产品ID
     * @return 预警记录
     */
    @PostMapping("/monitor/inventory-level/{productId}")
    public CommonResult<AlertRecord> monitorInventoryLevelByProductId(@PathVariable Long productId) {
        AlertRecord alertRecord = inventoryMonitorService.monitorInventoryLevelByProductId(productId);
        // 发送预警通知
        if (alertRecord != null) {
            alertNotificationService.sendAlertNotification(alertRecord);
        }
        return CommonResult.success(alertRecord);
    }

    /**
     * 计算库存周转率
     * @param productId 产品ID
     * @param days 统计天数
     * @return 库存周转率
     */
    @GetMapping("/calculate/turnover")
    public CommonResult<Double> calculateInventoryTurnover(@RequestParam Long productId, @RequestParam int days) {
        double turnover = inventoryMonitorService.calculateInventoryTurnover(productId, days);
        return CommonResult.success(turnover);
    }

    /**
     * 监控库存周转率
     * @param days 统计天数
     * @return 预警记录列表
     */
    @PostMapping("/monitor/turnover/{days}")
    public CommonResult<List<AlertRecord>> monitorInventoryTurnover(@PathVariable int days) {
        List<AlertRecord> alertRecords = inventoryMonitorService.monitorInventoryTurnover(days);
        // 发送预警通知
        if (!alertRecords.isEmpty()) {
            alertNotificationService.sendBatchAlertNotifications(alertRecords);
        }
        return CommonResult.success(alertRecords);
    }

    /**
     * 分析库龄
     * @param productId 产品ID
     * @return 库龄（天）
     */
    @GetMapping("/analyze/age/{productId}")
    public CommonResult<Integer> analyzeInventoryAge(@PathVariable Long productId) {
        int age = inventoryMonitorService.analyzeInventoryAge(productId);
        return CommonResult.success(age);
    }

    /**
     * 识别呆滞库存
     * @param maxAge 最大允许库龄（天）
     * @return 呆滞库存产品列表
     */
    @GetMapping("/identify/slow-moving/{maxAge}")
    public CommonResult<List<Product>> identifySlowMovingInventory(@PathVariable int maxAge) {
        List<Product> products = inventoryMonitorService.identifySlowMovingInventory(maxAge);
        return CommonResult.success(products);
    }

    /**
     * 生成库存健康报告
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 报告数据
     */
    @GetMapping("/report/inventory-health")
    public CommonResult<Map<String, Object>> generateInventoryHealthReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        Map<String, Object> reportData = reportService.generateInventoryHealthReport(startDate, endDate);
        return CommonResult.success(reportData);
    }

    /**
     * 导出库存健康报告为Excel
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Excel文件
     * @throws IOException IO异常
     */
    @GetMapping("/report/inventory-health/export")
    public ResponseEntity<byte[]> exportInventoryHealthReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) throws IOException {
        Workbook workbook = reportService.exportInventoryHealthReportToExcel(startDate, endDate);
        return exportExcel(workbook, "库存健康报告.xlsx");
    }

    /**
     * 生成缺货损失分析报告
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 报告数据
     */
    @GetMapping("/report/stockout-loss")
    public CommonResult<Map<String, Object>> generateStockoutLossReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        Map<String, Object> reportData = reportService.generateStockoutLossReport(startDate, endDate);
        return CommonResult.success(reportData);
    }

    /**
     * 导出缺货损失分析报告为Excel
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Excel文件
     * @throws IOException IO异常
     */
    @GetMapping("/report/stockout-loss/export")
    public ResponseEntity<byte[]> exportStockoutLossReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) throws IOException {
        Workbook workbook = reportService.exportStockoutLossReportToExcel(startDate, endDate);
        return exportExcel(workbook, "缺货损失分析报告.xlsx");
    }

    /**
     * 生成补货建议报告
     * @param forecastDays 预测天数
     * @return 补货建议列表
     */
    @GetMapping("/report/replenishment-suggestion")
    public CommonResult<List<Map<String, Object>>> generateReplenishmentSuggestionReport(@RequestParam int forecastDays) {
        List<Map<String, Object>> suggestions = reportService.generateReplenishmentSuggestionReport(forecastDays);
        return CommonResult.success(suggestions);
    }

    /**
     * 导出补货建议报告为Excel
     * @param forecastDays 预测天数
     * @return Excel文件
     * @throws IOException IO异常
     */
    @GetMapping("/report/replenishment-suggestion/export")
    public ResponseEntity<byte[]> exportReplenishmentSuggestionReport(@RequestParam int forecastDays) throws IOException {
        Workbook workbook = reportService.exportReplenishmentSuggestionReportToExcel(forecastDays);
        return exportExcel(workbook, "补货建议报告.xlsx");
    }

    /**
     * 导出Excel文件
     * @param workbook Excel工作簿
     * @param fileName 文件名
     * @return ResponseEntity
     * @throws IOException IO异常
     */
    private ResponseEntity<byte[]> exportExcel(Workbook workbook, String fileName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        byte[] bytes = outputStream.toByteArray();
        outputStream.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
