package zz.hujing.baseboot.service;

import org.apache.poi.ss.usermodel.Workbook;
import zz.hujing.baseboot.domain.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 统计分析报告服务接口
 */
public interface ReportService {

    /**
     * 生成库存健康报告
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 报告数据
     */
    Map<String, Object> generateInventoryHealthReport(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 生成缺货损失分析报告
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 报告数据
     */
    Map<String, Object> generateStockoutLossReport(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 生成补货建议报告
     * @param forecastDays 预测天数
     * @return 补货建议列表
     */
    List<Map<String, Object>> generateReplenishmentSuggestionReport(int forecastDays);

    /**
     * 导出库存健康报告为Excel
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Excel工作簿
     */
    Workbook exportInventoryHealthReportToExcel(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 导出缺货损失分析报告为Excel
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Excel工作簿
     */
    Workbook exportStockoutLossReportToExcel(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 导出补货建议报告为Excel
     * @param forecastDays 预测天数
     * @return Excel工作簿
     */
    Workbook exportReplenishmentSuggestionReportToExcel(int forecastDays);
}
