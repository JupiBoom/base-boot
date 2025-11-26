package zz.hujing.baseboot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.SalesRecord;
import zz.hujing.baseboot.repository.SalesRecordRepository;
import zz.hujing.baseboot.service.DemandForecastService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 需求预测服务实现类
 */
@Slf4j
@Service
public class DemandForecastServiceImpl implements DemandForecastService {

    @Autowired
    private SalesRecordRepository salesRecordRepository;

    /**
     * 基于移动平均法预测销量
     * @param productId 产品ID
     * @param days 历史数据天数
     * @param forecastDays 预测天数
     * @return 预测结果（日期 -> 销量）
     */
    @Override
    public Map<LocalDateTime, Integer> forecastSalesByMovingAverage(Long productId, int days, int forecastDays) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<SalesRecord> salesRecords = salesRecordRepository.findRecentSalesByProductId(productId, startDate);

        if (salesRecords.isEmpty()) {
            log.warn("没有找到足够的历史销售数据，productId: {}", productId);
            return new HashMap<>();
        }

        // 按日期分组统计销量
        Map<LocalDateTime, Integer> dailySales = new TreeMap<>();
        for (SalesRecord record : salesRecords) {
            LocalDateTime date = record.getSalesDate().truncatedTo(ChronoUnit.DAYS);
            dailySales.put(date, dailySales.getOrDefault(date, 0) + record.getSalesQuantity());
        }

        // 计算移动平均
        List<Integer> salesList = new ArrayList<>(dailySales.values());
        int n = salesList.size();
        if (n < 3) {
            log.warn("历史销售数据不足，无法进行移动平均预测，productId: {}", productId);
            return new HashMap<>();
        }

        // 使用3天移动平均
        List<Double> movingAverages = new ArrayList<>();
        for (int i = 0; i <= n - 3; i++) {
            double average = (salesList.get(i) + salesList.get(i + 1) + salesList.get(i + 2)) / 3.0;
            movingAverages.add(average);
        }

        // 预测未来销量
        Map<LocalDateTime, Integer> forecastResult = new TreeMap<>();
        LocalDateTime lastDate = dailySales.keySet().stream().max(LocalDateTime::compareTo).orElse(LocalDateTime.now());
        double lastAverage = movingAverages.get(movingAverages.size() - 1);

        for (int i = 1; i <= forecastDays; i++) {
            LocalDateTime forecastDate = lastDate.plusDays(i);
            int forecastSales = (int) Math.round(lastAverage);
            forecastResult.put(forecastDate, forecastSales);
        }

        return forecastResult;
    }

    /**
     * 基于简单线性回归预测销量趋势
     * @param productId 产品ID
     * @param days 历史数据天数
     * @param forecastDays 预测天数
     * @return 预测结果（日期 -> 销量）
     */
    @Override
    public Map<LocalDateTime, Integer> forecastSalesByLinearRegression(Long productId, int days, int forecastDays) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<SalesRecord> salesRecords = salesRecordRepository.findRecentSalesByProductId(productId, startDate);

        if (salesRecords.isEmpty()) {
            log.warn("没有找到足够的历史销售数据，productId: {}", productId);
            return new HashMap<>();
        }

        // 按日期分组统计销量
        Map<LocalDateTime, Integer> dailySales = new TreeMap<>();
        for (SalesRecord record : salesRecords) {
            LocalDateTime date = record.getSalesDate().truncatedTo(ChronoUnit.DAYS);
            dailySales.put(date, dailySales.getOrDefault(date, 0) + record.getSalesQuantity());
        }

        List<LocalDateTime> dates = new ArrayList<>(dailySales.keySet());
        List<Integer> salesList = new ArrayList<>(dailySales.values());
        int n = salesList.size();

        if (n < 2) {
            log.warn("历史销售数据不足，无法进行线性回归预测，productId: {}", productId);
            return new HashMap<>();
        }

        // 计算回归系数
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        for (int i = 0; i < n; i++) {
            double x = i + 1;
            double y = salesList.get(i);
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;

        // 预测未来销量
        Map<LocalDateTime, Integer> forecastResult = new TreeMap<>();
        LocalDateTime lastDate = dates.get(dates.size() - 1);

        for (int i = 1; i <= forecastDays; i++) {
            LocalDateTime forecastDate = lastDate.plusDays(i);
            double x = n + i;
            double forecastSales = slope * x + intercept;
            forecastResult.put(forecastDate, (int) Math.round(forecastSales));
        }

        return forecastResult;
    }

    /**
     * 应用季节性指数调整预测结果
     * @param forecastResult 原始预测结果
     * @param productId 产品ID
     * @return 调整后的预测结果
     */
    @Override
    public Map<LocalDateTime, Integer> adjustForecastBySeasonalIndex(Map<LocalDateTime, Integer> forecastResult, Long productId) {
        if (forecastResult.isEmpty()) {
            return forecastResult;
        }

        // 计算季节性指数
        Map<Integer, Double> seasonalIndex = calculateSeasonalIndex(productId, 2);

        // 应用季节性指数调整预测结果
        Map<LocalDateTime, Integer> adjustedResult = new TreeMap<>();
        for (Map.Entry<LocalDateTime, Integer> entry : forecastResult.entrySet()) {
            LocalDateTime date = entry.getKey();
            int month = date.getMonthValue();
            double index = seasonalIndex.getOrDefault(month, 1.0);
            int adjustedSales = (int) Math.round(entry.getValue() * index);
            adjustedResult.put(date, adjustedSales);
        }

        return adjustedResult;
    }

    /**
     * 计算季节性指数
     * @param productId 产品ID
     * @param years 历史年份数
     * @return 季节性指数（月份 -> 指数）
     */
    @Override
    public Map<Integer, Double> calculateSeasonalIndex(Long productId, int years) {
        LocalDateTime startDate = LocalDateTime.now().minusYears(years);
        List<SalesRecord> salesRecords = salesRecordRepository.findRecentSalesByProductId(productId, startDate);

        if (salesRecords.isEmpty()) {
            log.warn("没有找到足够的历史销售数据来计算季节性指数，productId: {}", productId);
            return new HashMap<>();
        }

        // 按年月分组统计销量
        Map<String, Integer> monthlySales = new HashMap<>();
        for (SalesRecord record : salesRecords) {
            LocalDateTime date = record.getSalesDate();
            String key = String.format("%d-%d", date.getYear(), date.getMonthValue());
            monthlySales.put(key, monthlySales.getOrDefault(key, 0) + record.getSalesQuantity());
        }

        // 计算各年每月的销量
        Map<Integer, List<Integer>> monthlyData = new HashMap<>();
        for (Map.Entry<String, Integer> entry : monthlySales.entrySet()) {
            String[] parts = entry.getKey().split("-");
            int month = Integer.parseInt(parts[1]);
            int sales = entry.getValue();
            monthlyData.computeIfAbsent(month, k -> new ArrayList<>()).add(sales);
        }

        // 计算每月的平均销量
        Map<Integer, Double> monthlyAverage = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : monthlyData.entrySet()) {
            List<Integer> salesList = entry.getValue();
            double average = salesList.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            monthlyAverage.put(entry.getKey(), average);
        }

        // 计算总平均销量
        double totalAverage = monthlyAverage.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        // 计算季节性指数
        Map<Integer, Double> seasonalIndex = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : monthlyAverage.entrySet()) {
            if (totalAverage != 0) {
                double index = entry.getValue() / totalAverage;
                seasonalIndex.put(entry.getKey(), index);
            } else {
                seasonalIndex.put(entry.getKey(), 1.0);
            }
        }

        return seasonalIndex;
    }
}
