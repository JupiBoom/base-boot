package zz.hujing.baseboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.forecast.DemandForecast;
import zz.hujing.baseboot.domain.sales.SalesRecord;
import zz.hujing.baseboot.repository.DemandForecastRepository;
import zz.hujing.baseboot.repository.SalesRecordRepository;
import zz.hujing.baseboot.service.DemandForecastService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemandForecastServiceImpl implements DemandForecastService {

    private final SalesRecordRepository salesRecordRepository;
    private final DemandForecastRepository demandForecastRepository;

    @Override
    public List<DemandForecast> forecastByMovingAverage(Long productId, Integer periodDays, Integer forecastDays) {
        List<DemandForecast> forecasts = new ArrayList<>();

        // 获取历史销售数据
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(periodDays * 2); // 获取足够的历史数据
        List<SalesRecord> salesRecords = salesRecordRepository.findByProductIdAndSaleDateBetween(
                productId, startDate, endDate);

        if (salesRecords.isEmpty()) {
            return forecasts;
        }

        // 计算移动平均
        int n = salesRecords.size();
        double sum = salesRecords.stream().mapToInt(SalesRecord::getSaleQuantity).sum();
        double movingAverage = sum / n;

        // 生成预测结果
        for (int i = 1; i <= forecastDays; i++) {
            DemandForecast forecast = new DemandForecast();
            forecast.setProductId(productId);
            forecast.setForecastDate(endDate.plusDays(i));
            forecast.setForecastPeriod(forecastDays);
            forecast.setForecastQuantity((int) Math.round(movingAverage));
            forecast.setForecastMethod("移动平均法");
            forecasts.add(forecast);
        }

        // 保存预测结果
        demandForecastRepository.saveAll(forecasts);

        return forecasts;
    }

    @Override
    public List<DemandForecast> forecastByLinearRegression(Long productId, Integer historicalDays, Integer forecastDays) {
        List<DemandForecast> forecasts = new ArrayList<>();

        // 获取历史销售数据
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(historicalDays);
        List<SalesRecord> salesRecords = salesRecordRepository.findByProductIdAndSaleDateBetween(
                productId, startDate, endDate);

        if (salesRecords.size() < 2) {
            return forecasts;
        }

        // 简单线性回归：y = a + bx
        int n = salesRecords.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            double x = i + 1;
            double y = salesRecords.get(i).getSaleQuantity();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double b = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double a = (sumY - b * sumX) / n;

        // 生成预测结果
        LocalDate forecastStartDate = endDate.plusDays(1);
        for (int i = 0; i < forecastDays; i++) {
            double x = n + 1 + i;
            int forecastQuantity = (int) Math.round(a + b * x);

            DemandForecast forecast = new DemandForecast();
            forecast.setProductId(productId);
            forecast.setForecastDate(forecastStartDate.plusDays(i));
            forecast.setForecastPeriod(forecastDays);
            forecast.setForecastQuantity(Math.max(forecastQuantity, 0)); // 确保预测数量不为负
            forecast.setForecastMethod("简单线性回归");
            forecasts.add(forecast);
        }

        // 保存预测结果
        demandForecastRepository.saveAll(forecasts);

        return forecasts;
    }

    @Override
    public List<DemandForecast> adjustWithSeasonalIndex(List<DemandForecast> forecasts, Long productId) {
        // 简化实现：这里应该根据历史数据计算季节性指数
        // 为了演示，我们假设每个月的季节性指数都是1.0，节假日可能有调整
        for (DemandForecast forecast : forecasts) {
            // 假设节假日系数为1.2
            int dayOfWeek = forecast.getForecastDate().getDayOfWeek().getValue();
            if (dayOfWeek == 6 || dayOfWeek == 7) { // 周末
                forecast.setForecastQuantity((int) Math.round(forecast.getForecastQuantity() * 1.2));
            }
        }

        return forecasts;
    }

    @Override
    public List<DemandForecast> getHistoricalForecasts(Long productId, LocalDate startDate, LocalDate endDate) {
        return demandForecastRepository.findByProductIdAndForecastDateBetween(productId, startDate, endDate);
    }
}