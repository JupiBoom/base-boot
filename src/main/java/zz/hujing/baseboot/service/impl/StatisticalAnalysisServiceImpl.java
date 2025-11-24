package zz.hujing.baseboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.inventory.Inventory;
import zz.hujing.baseboot.domain.product.Product;
import zz.hujing.baseboot.repository.InventoryRepository;
import zz.hujing.baseboot.repository.ProductRepository;
import zz.hujing.baseboot.service.StatisticalAnalysisService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticalAnalysisServiceImpl implements StatisticalAnalysisService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public Map<String, Object> generateInventoryHealthReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();

        List<Product> products = productRepository.findAll();
        List<Inventory> inventories = inventoryRepository.findAll();

        // 计算库存健康指标
        int totalProducts = products.size();
        int lowStockProducts = 0;
        int outOfStockProducts = 0;
        int slowMovingProducts = 0;

        for (Product product : products) {
            Inventory inventory = inventories.stream()
                    .filter(inv -> inv.getProductId().equals(product.getId()))
                    .findFirst()
                    .orElse(null);

            if (inventory != null) {
                if (inventory.getAvailableStock() == 0) {
                    outOfStockProducts++;
                } else if (inventory.getAvailableStock() < product.getSafetyStock()) {
                    lowStockProducts++;
                }

                // 简单判断呆滞库存（30天未销售）
                if (inventory.getLastSaleTime() != null && 
                    inventory.getLastSaleTime().toLocalDate().isBefore(LocalDate.now().minusDays(30))) {
                    slowMovingProducts++;
                }
            }
        }

        report.put("totalProducts", totalProducts);
        report.put("lowStockProducts", lowStockProducts);
        report.put("outOfStockProducts", outOfStockProducts);
        report.put("slowMovingProducts", slowMovingProducts);
        report.put("lowStockRate", totalProducts > 0 ? (double) lowStockProducts / totalProducts : 0);
        report.put("outOfStockRate", totalProducts > 0 ? (double) outOfStockProducts / totalProducts : 0);
        report.put("slowMovingRate", totalProducts > 0 ? (double) slowMovingProducts / totalProducts : 0);
        report.put("startDate", startDate);
        report.put("endDate", endDate);

        return report;
    }

    @Override
    public Map<String, Object> generateStockoutLossAnalysis(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();

        // 简化实现：这里应该根据销售数据和库存数据计算缺货损失
        // 为了演示，返回模拟数据
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalStockoutEvents", 5);
        report.put("estimatedLossAmount", 15000.0);
        report.put("mostAffectedProducts", List.of("Product A", "Product B", "Product C"));

        return report;
    }

    @Override
    public Map<String, Object> generateReplenishmentSuggestion(Long productId) {
        Map<String, Object> report = new HashMap<>();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        // 简单的补货建议计算
        int safetyStock = product.getSafetyStock();
        int currentStock = inventory.getAvailableStock();
        int leadTime = product.getLeadTimeDays();

        // 假设日均销量为10
        int dailySales = 10;
        int reorderPoint = safetyStock + (dailySales * leadTime);
        int suggestedReplenishment = reorderPoint - currentStock;

        report.put("productId", productId);
        report.put("productName", product.getProductName());
        report.put("currentStock", currentStock);
        report.put("safetyStock", safetyStock);
        report.put("leadTimeDays", leadTime);
        report.put("dailySales", dailySales);
        report.put("reorderPoint", reorderPoint);
        report.put("suggestedReplenishment", Math.max(suggestedReplenishment, 0));

        return report;
    }

    @Override
    public Workbook exportInventoryHealthReportToExcel(LocalDate startDate, LocalDate endDate) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("库存健康报告");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"指标", "数值"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            // 设置表头样式
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // 填充数据
        Map<String, Object> reportData = generateInventoryHealthReport(startDate, endDate);
        int rowIndex = 1;

        addReportRow(sheet, rowIndex++, "统计周期", startDate + " 至 " + endDate);
        addReportRow(sheet, rowIndex++, "总产品数", reportData.get("totalProducts"));
        addReportRow(sheet, rowIndex++, "低库存产品数", reportData.get("lowStockProducts"));
        addReportRow(sheet, rowIndex++, "缺货产品数", reportData.get("outOfStockProducts"));
        addReportRow(sheet, rowIndex++, "呆滞库存产品数", reportData.get("slowMovingProducts"));
        addReportRow(sheet, rowIndex++, "低库存率", String.format("%.2f%%", (double) reportData.get("lowStockRate") * 100));
        addReportRow(sheet, rowIndex++, "缺货率", String.format("%.2f%%", (double) reportData.get("outOfStockRate") * 100));
        addReportRow(sheet, rowIndex++, "呆滞库存率", String.format("%.2f%%", (double) reportData.get("slowMovingRate") * 100));

        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    @Override
    public Workbook exportReplenishmentSuggestionToExcel(Long productId) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("补货建议报告");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"产品信息", "数值"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            // 设置表头样式
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // 填充数据
        Map<String, Object> reportData = generateReplenishmentSuggestion(productId);
        int rowIndex = 1;

        addReportRow(sheet, rowIndex++, "产品ID", reportData.get("productId"));
        addReportRow(sheet, rowIndex++, "产品名称", reportData.get("productName"));
        addReportRow(sheet, rowIndex++, "当前库存", reportData.get("currentStock"));
        addReportRow(sheet, rowIndex++, "安全库存", reportData.get("safetyStock"));
        addReportRow(sheet, rowIndex++, "采购提前期（天）", reportData.get("leadTimeDays"));
        addReportRow(sheet, rowIndex++, "日均销量", reportData.get("dailySales"));
        addReportRow(sheet, rowIndex++, "再订货点", reportData.get("reorderPoint"));
        addReportRow(sheet, rowIndex++, "建议补货量", reportData.get("suggestedReplenishment"));

        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    private void addReportRow(Sheet sheet, int rowIndex, String label, Object value) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(label);
        if (value instanceof String) {
            row.createCell(1).setCellValue((String) value);
        } else if (value instanceof Number) {
            row.createCell(1).setCellValue(((Number) value).doubleValue());
        }
    }
}