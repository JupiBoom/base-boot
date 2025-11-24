package zz.hujing.baseboot.service;

import org.apache.poi.ss.usermodel.Workbook;
import zz.hujing.baseboot.domain.inventory.Inventory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatisticalAnalysisService {
    /**
     * 生成库存健康报告
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 报告数据
     */
    Map<String, Object> generateInventoryHealthReport(LocalDate startDate, LocalDate endDate);

    /**
     * 生成缺货损失分析报告
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 报告数据
     */
    Map<String, Object> generateStockoutLossAnalysis(LocalDate startDate, LocalDate endDate);

    /**
     * 生成补货建议报告
     * @param productId 产品ID
     * @return 报告数据
     */
    Map<String, Object> generateReplenishmentSuggestion(Long productId);

    /**
     * 导出库存健康报告为Excel
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Excel工作簿
     */
    Workbook exportInventoryHealthReportToExcel(LocalDate startDate, LocalDate endDate);

    /**
     * 导出补货建议报告为Excel
     * @param productId 产品ID
     * @return Excel工作簿
     */
    Workbook exportReplenishmentSuggestionToExcel(Long productId);
}