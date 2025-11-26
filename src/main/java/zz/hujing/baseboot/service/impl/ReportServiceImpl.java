package zz.hujing.baseboot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.Inventory;
import zz.hujing.baseboot.domain.Product;
import zz.hujing.baseboot.domain.SalesRecord;
import zz.hujing.baseboot.repository.InventoryRepository;
import zz.hujing.baseboot.repository.ProductRepository;
import zz.hujing.baseboot.repository.SalesRecordRepository;
import zz.hujing.baseboot.service.DemandForecastService;
import zz.hujing.baseboot.service.InventoryMonitorService;
import zz.hujing.baseboot.service.ReportService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 统计分析报告服务实现类
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private SalesRecordRepository salesRecordRepository;

    @Autowired
    private DemandForecastService demandForecastService;

    @Autowired
    private InventoryMonitorService inventoryMonitorService;

    /**
     * 生成库存健康报告
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 报告数据
     */
    @Override
    public Map<String, Object> generateInventoryHealthReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> reportData = new HashMap<>();

        // 获取所有产品
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            reportData.put("message", "没有产品数据");
            return reportData;
        }

        // 统计库存健康指标
        int totalProducts = products.size();
        int lowStockProducts = 0;
        int slowMovingProducts = 0;
        int normalProducts = 0;

        List<Map<String, Object>> productDetails = new ArrayList<>();

        for (Product product : products) {
            Inventory inventory = inventoryRepository.findByProductId(product.getId());
            if (inventory == null) {
                continue;
            }

            Map<String, Object> productDetail = new HashMap<>();
            productDetail.put("productName", product.getProductName());
            productDetail.put("productCode", product.getProductCode());
            productDetail.put("currentStock", inventory.getCurrentStock());
            productDetail.put("safetyStock", product.getSafetyStock());

            // 计算库存周转率
            long days = ChronoUnit.DAYS.between(startDate, endDate);
            double turnover = days > 0 ? inventoryMonitorService.calculateInventoryTurnover(product.getId(), (int) days) : 0.0;
            productDetail.put("inventoryTurnover", turnover);

            // 分析库龄
            int inventoryAge = 0;
            if (inventory.getLastInTime() != null) {
                inventoryAge = (int) ChronoUnit.DAYS.between(inventory.getLastInTime(), LocalDateTime.now());
            }
            productDetail.put("inventoryAge", inventoryAge);

            // 判断库存状态
            String status;
            if (inventory.getCurrentStock() <= product.getSafetyStock()) {
                status = "库存不足";
                lowStockProducts++;
            } else if (inventoryAge > 90) { // 假设90天以上为呆滞库存
                status = "呆滞库存";
                slowMovingProducts++;
            } else {
                status = "正常";
                normalProducts++;
            }
            productDetail.put("status", status);

            productDetails.add(productDetail);
        }

        // 填充报告数据
        reportData.put("startDate", startDate);
        reportData.put("endDate", endDate);
        reportData.put("totalProducts", totalProducts);
        reportData.put("lowStockProducts", lowStockProducts);
        reportData.put("slowMovingProducts", slowMovingProducts);
        reportData.put("normalProducts", normalProducts);
        reportData.put("productDetails", productDetails);

        return reportData;
    }

    /**
     * 生成缺货损失分析报告
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 报告数据
     */
    @Override
    public Map<String, Object> generateStockoutLossReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> reportData = new HashMap<>();

        // 获取所有产品
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            reportData.put("message", "没有产品数据");
            return reportData;
        }

        // 统计缺货损失
        double totalLoss = 0.0;
        List<Map<String, Object>> productLossDetails = new ArrayList<>();

        for (Product product : products) {
            Inventory inventory = inventoryRepository.findByProductId(product.getId());
            if (inventory == null) {
                continue;
            }

            // 查询销售记录
            List<SalesRecord> salesRecords = salesRecordRepository.findByProductIdAndSalesDateBetween(product.getId(), startDate, endDate);
            if (salesRecords.isEmpty()) {
                continue;
            }

            // 计算实际销售金额
            double actualSales = salesRecords.stream().mapToDouble(record -> record.getSalesAmount().doubleValue()).sum();

            // 假设如果库存充足，销量会增加20%
            double potentialSales = actualSales * 1.2;
            double loss = potentialSales - actualSales;
            totalLoss += loss;

            // 填充产品损失详情
            Map<String, Object> productLossDetail = new HashMap<>();
            productLossDetail.put("productName", product.getProductName());
            productLossDetail.put("productCode", product.getProductCode());
            productLossDetail.put("actualSales", actualSales);
            productLossDetail.put("potentialSales", potentialSales);
            productLossDetail.put("loss", loss);

            productLossDetails.add(productLossDetail);
        }

        // 填充报告数据
        reportData.put("startDate", startDate);
        reportData.put("endDate", endDate);
        reportData.put("totalLoss", totalLoss);
        reportData.put("productLossDetails", productLossDetails);

        return reportData;
    }

    /**
     * 生成补货建议报告
     * @param forecastDays 预测天数
     * @return 补货建议列表
     */
    @Override
    public List<Map<String, Object>> generateReplenishmentSuggestionReport(int forecastDays) {
        List<Map<String, Object>> suggestions = new ArrayList<>();

        // 获取所有产品
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return suggestions;
        }

        for (Product product : products) {
            Inventory inventory = inventoryRepository.findByProductId(product.getId());
            if (inventory == null) {
                continue;
            }

            // 预测未来销量
            Map<LocalDateTime, Integer> forecastResult = demandForecastService.forecastSalesByMovingAverage(product.getId(), 30, forecastDays);
            if (forecastResult.isEmpty()) {
                continue;
            }

            // 计算未来总销量
            int totalForecastSales = forecastResult.values().stream().mapToInt(Integer::intValue).sum();

            // 计算补货量
            int currentStock = inventory.getCurrentStock();
            int safetyStock = product.getSafetyStock();
            int requiredStock = totalForecastSales + safetyStock;
            int replenishmentQuantity = requiredStock - currentStock;

            if (replenishmentQuantity > 0) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("productName", product.getProductName());
                suggestion.put("productCode", product.getProductCode());
                suggestion.put("currentStock", currentStock);
                suggestion.put("safetyStock", safetyStock);
                suggestion.put("forecastSales", totalForecastSales);
                suggestion.put("requiredStock", requiredStock);
                suggestion.put("replenishmentQuantity", replenishmentQuantity);
                suggestion.put("forecastDays", forecastDays);

                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }

    /**
     * 导出库存健康报告为Excel
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Excel工作簿
     */
    @Override
    public Workbook exportInventoryHealthReportToExcel(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> reportData = generateInventoryHealthReport(startDate, endDate);
        Workbook workbook = new XSSFWorkbook();

        // 创建工作表
        Sheet sheet = workbook.createSheet("库存健康报告");

        // 创建标题行
        Row titleRow = sheet.createRow(0);
        String[] titles = {"产品名称", "产品编码", "当前库存", "安全库存", "库存周转率", "库龄(天)", "状态"};
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
            // 设置单元格样式
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            cell.setCellStyle(style);
        }

        // 填充数据行
        List<Map<String, Object>> productDetails = (List<Map<String, Object>>) reportData.get("productDetails");
        if (productDetails != null && !productDetails.isEmpty()) {
            for (int i = 0; i < productDetails.size(); i++) {
                Map<String, Object> productDetail = productDetails.get(i);
                Row dataRow = sheet.createRow(i + 1);

                dataRow.createCell(0).setCellValue((String) productDetail.get("productName"));
                dataRow.createCell(1).setCellValue((String) productDetail.get("productCode"));
                dataRow.createCell(2).setCellValue((Integer) productDetail.get("currentStock"));
                dataRow.createCell(3).setCellValue((Integer) productDetail.get("safetyStock"));
                dataRow.createCell(4).setCellValue((Double) productDetail.get("inventoryTurnover"));
                dataRow.createCell(5).setCellValue((Integer) productDetail.get("inventoryAge"));
                dataRow.createCell(6).setCellValue((String) productDetail.get("status"));

                // 设置数据行样式
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = dataRow.getCell(j);
                    CellStyle style = workbook.createCellStyle();
                    style.setAlignment(HorizontalAlignment.CENTER);
                    style.setVerticalAlignment(VerticalAlignment.CENTER);
                    style.setBorderTop(BorderStyle.THIN);
                    style.setBorderBottom(BorderStyle.THIN);
                    style.setBorderLeft(BorderStyle.THIN);
                    style.setBorderRight(BorderStyle.THIN);
                    cell.setCellStyle(style);
                }
            }
        }

        // 自动调整列宽
        for (int i = 0; i < titles.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    /**
     * 导出缺货损失分析报告为Excel
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Excel工作簿
     */
    @Override
    public Workbook exportStockoutLossReportToExcel(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> reportData = generateStockoutLossReport(startDate, endDate);
        Workbook workbook = new XSSFWorkbook();

        // 创建工作表
        Sheet sheet = workbook.createSheet("缺货损失分析报告");

        // 创建标题行
        Row titleRow = sheet.createRow(0);
        String[] titles = {"产品名称", "产品编码", "实际销售金额", "潜在销售金额", "损失金额"};
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
            // 设置单元格样式
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            cell.setCellStyle(style);
        }

        // 填充数据行
        List<Map<String, Object>> productLossDetails = (List<Map<String, Object>>) reportData.get("productLossDetails");
        if (productLossDetails != null && !productLossDetails.isEmpty()) {
            for (int i = 0; i < productLossDetails.size(); i++) {
                Map<String, Object> productLossDetail = productLossDetails.get(i);
                Row dataRow = sheet.createRow(i + 1);

                dataRow.createCell(0).setCellValue((String) productLossDetail.get("productName"));
                dataRow.createCell(1).setCellValue((String) productLossDetail.get("productCode"));
                dataRow.createCell(2).setCellValue((Double) productLossDetail.get("actualSales"));
                dataRow.createCell(3).setCellValue((Double) productLossDetail.get("potentialSales"));
                dataRow.createCell(4).setCellValue((Double) productLossDetail.get("loss"));

                // 设置数据行样式
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = dataRow.getCell(j);
                    CellStyle style = workbook.createCellStyle();
                    style.setAlignment(HorizontalAlignment.CENTER);
                    style.setVerticalAlignment(VerticalAlignment.CENTER);
                    style.setBorderTop(BorderStyle.THIN);
                    style.setBorderBottom(BorderStyle.THIN);
                    style.setBorderLeft(BorderStyle.THIN);
                    style.setBorderRight(BorderStyle.THIN);
                    cell.setCellStyle(style);
                }
            }
        }

        // 自动调整列宽
        for (int i = 0; i < titles.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    /**
     * 导出补货建议报告为Excel
     * @param forecastDays 预测天数
     * @return Excel工作簿
     */
    @Override
    public Workbook exportReplenishmentSuggestionReportToExcel(int forecastDays) {
        List<Map<String, Object>> suggestions = generateReplenishmentSuggestionReport(forecastDays);
        Workbook workbook = new XSSFWorkbook();

        // 创建工作表
        Sheet sheet = workbook.createSheet("补货建议报告");

        // 创建标题行
        Row titleRow = sheet.createRow(0);
        String[] titles = {"产品名称", "产品编码", "当前库存", "安全库存", "预测销量", "所需库存", "补货量", "预测天数"};
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
            // 设置单元格样式
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            cell.setCellStyle(style);
        }

        // 填充数据行
        if (suggestions != null && !suggestions.isEmpty()) {
            for (int i = 0; i < suggestions.size(); i++) {
                Map<String, Object> suggestion = suggestions.get(i);
                Row dataRow = sheet.createRow(i + 1);

                dataRow.createCell(0).setCellValue((String) suggestion.get("productName"));
                dataRow.createCell(1).setCellValue((String) suggestion.get("productCode"));
                dataRow.createCell(2).setCellValue((Integer) suggestion.get("currentStock"));
                dataRow.createCell(3).setCellValue((Integer) suggestion.get("safetyStock"));
                dataRow.createCell(4).setCellValue((Integer) suggestion.get("forecastSales"));
                dataRow.createCell(5).setCellValue((Integer) suggestion.get("requiredStock"));
                dataRow.createCell(6).setCellValue((Integer) suggestion.get("replenishmentQuantity"));
                dataRow.createCell(7).setCellValue((Integer) suggestion.get("forecastDays"));

                // 设置数据行样式
                for (int j = 0; j < titles.length; j++) {
                    Cell cell = dataRow.getCell(j);
                    CellStyle style = workbook.createCellStyle();
                    style.setAlignment(HorizontalAlignment.CENTER);
                    style.setVerticalAlignment(VerticalAlignment.CENTER);
                    style.setBorderTop(BorderStyle.THIN);
                    style.setBorderBottom(BorderStyle.THIN);
                    style.setBorderLeft(BorderStyle.THIN);
                    style.setBorderRight(BorderStyle.THIN);
                    cell.setCellStyle(style);
                }
            }
        }

        // 自动调整列宽
        for (int i = 0; i < titles.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }
}
