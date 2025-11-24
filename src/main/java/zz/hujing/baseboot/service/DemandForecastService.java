package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.forecast.DemandForecast;

import java.time.LocalDate;
import java.util.List;

public interface DemandForecastService {
    /**
     * 基于移动平均法预测销量
     * @param productId 产品ID
     * @param periodDays 移动平均周期（天）
     * @param forecastDays 预测天数
     * @return 预测结果列表
     */
    List<DemandForecast> forecastByMovingAverage(Long productId, Integer periodDays, Integer forecastDays);

    /**
     * 基于简单线性回归预测销量趋势
     * @param productId 产品ID
     * @param historicalDays 历史数据天数
     * @param forecastDays 预测天数
     * @return 预测结果列表
     */
    List<DemandForecast> forecastByLinearRegression(Long productId, Integer historicalDays, Integer forecastDays);

    /**
     * 应用季节性指数调整预测结果
     * @param forecasts 原始预测结果
     * @param productId 产品ID
     * @return 调整后的预测结果
     */
    List<DemandForecast> adjustWithSeasonalIndex(List<DemandForecast> forecasts, Long productId);

    /**
     * 获取产品的历史预测记录
     * @param productId 产品ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 历史预测记录
     */
    List<DemandForecast> getHistoricalForecasts(Long productId, LocalDate startDate, LocalDate endDate);
}