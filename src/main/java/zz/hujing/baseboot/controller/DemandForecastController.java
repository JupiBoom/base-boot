package zz.hujing.baseboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.domain.forecast.DemandForecast;
import zz.hujing.baseboot.service.DemandForecastService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/forecast")
@RequiredArgsConstructor
public class DemandForecastController {

    private final DemandForecastService demandForecastService;

    @GetMapping("/moving-average/{productId}")
    public List<DemandForecast> forecastByMovingAverage(@PathVariable Long productId,
                                                       @RequestParam(defaultValue = "30") Integer periodDays,
                                                       @RequestParam(defaultValue = "7") Integer forecastDays) {
        return demandForecastService.forecastByMovingAverage(productId, periodDays, forecastDays);
    }

    @GetMapping("/linear-regression/{productId}")
    public List<DemandForecast> forecastByLinearRegression(@PathVariable Long productId,
                                                          @RequestParam(defaultValue = "60") Integer historicalDays,
                                                          @RequestParam(defaultValue = "7") Integer forecastDays) {
        return demandForecastService.forecastByLinearRegression(productId, historicalDays, forecastDays);
    }

    @PostMapping("/adjust-seasonal")
    public List<DemandForecast> adjustWithSeasonalIndex(@RequestBody List<DemandForecast> forecasts,
                                                       @RequestParam Long productId) {
        return demandForecastService.adjustWithSeasonalIndex(forecasts, productId);
    }

    @GetMapping("/historical/{productId}")
    public List<DemandForecast> getHistoricalForecasts(@PathVariable Long productId,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return demandForecastService.getHistoricalForecasts(productId, startDate, endDate);
    }
}