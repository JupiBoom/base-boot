package zz.hujing.baseboot.service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 需求预测服务接口
 */
public interface DemandForecastService {

    /**
     * 基于移动平均法预测销量
     * @param productId 产品ID
     * @param days 历史数据天数
     * @param forecastDays 预测天数
     * @return 预测结果（日期 -> 销量）
     */
    Map<LocalDateTime, Integer> forecastSalesByMovingAverage(Long productId, int days, int forecastDays);

    /**
     * 基于简单线性回归预测销量趋势
     * @param productId 产品ID
     * @param days 历史数据天数
     * @param forecastDays 预测天数
     * @return 预测结果（日期 -> 销量）
     */
    Map<LocalDateTime, Integer> forecastSalesByLinearRegression(Long productId, int days, int forecastDays);

    /**
     * 应用季节性指数调整预测结果
     * @param forecastResult 原始预测结果
     * @param productId 产品ID
     * @return 调整后的预测结果
     */
    Map<LocalDateTime, Integer> adjustForecastBySeasonalIndex(Map<LocalDateTime, Integer> forecastResult, Long productId);

    /**
     * 计算季节性指数
     * @param productId 产品ID
     * @param years 历史年份数
     * @return 季节性指数（月份 -> 指数）
     */
    Map<Integer, Double> calculateSeasonalIndex(Long productId, int years);
}
