package zz.hujing.baseboot.controller;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.service.StatisticalAnalysisService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class StatisticalAnalysisController {

    private final StatisticalAnalysisService statisticalAnalysisService;

    @GetMapping("/inventory-health")
    public Map<String, Object> generateInventoryHealthReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return statisticalAnalysisService.generateInventoryHealthReport(startDate, endDate);
    }

    @GetMapping("/stockout-loss")
    public Map<String, Object> generateStockoutLossAnalysis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return statisticalAnalysisService.generateStockoutLossAnalysis(startDate, endDate);
    }

    @GetMapping("/replenishment-suggestion/{productId}")
    public Map<String, Object> generateReplenishmentSuggestion(@PathVariable Long productId) {
        return statisticalAnalysisService.generateReplenishmentSuggestion(productId);
    }

    @GetMapping("/export/inventory-health")
    public ResponseEntity<byte[]> exportInventoryHealthReportToExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws IOException {
        Workbook workbook = statisticalAnalysisService.exportInventoryHealthReportToExcel(startDate, endDate);
        return exportExcel(workbook, String.format("库存健康报告_%s至%s.xlsx", startDate, endDate));
    }

    @GetMapping("/export/replenishment-suggestion/{productId}")
    public ResponseEntity<byte[]> exportReplenishmentSuggestionToExcel(@PathVariable Long productId) throws IOException {
        Workbook workbook = statisticalAnalysisService.exportReplenishmentSuggestionToExcel(productId);
        return exportExcel(workbook, String.format("补货建议报告_%d.xlsx", productId));
    }

    private ResponseEntity<byte[]> exportExcel(Workbook workbook, String filename) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
}