package zz.hujing.baseboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.domain.alert.AlertRecord;
import zz.hujing.baseboot.domain.inventory.Inventory;
import zz.hujing.baseboot.service.InventoryMonitorService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory/monitor")
@RequiredArgsConstructor
public class InventoryMonitorController {

    private final InventoryMonitorService inventoryMonitorService;

    @PostMapping("/check-levels")
    public List<AlertRecord> checkInventoryLevels() {
        return inventoryMonitorService.monitorInventoryLevels();
    }

    @GetMapping("/turnover/{productId}")
    public Double calculateInventoryTurnover(@PathVariable Long productId, 
                                             @RequestParam(defaultValue = "30") Integer periodDays) {
        return inventoryMonitorService.calculateInventoryTurnover(productId, periodDays);
    }

    @GetMapping("/age/{productId}")
    public Map<String, Object> analyzeInventoryAge(@PathVariable Long productId) {
        return inventoryMonitorService.analyzeInventoryAge(productId);
    }

    @GetMapping("/slow-moving")
    public List<Inventory> identifySlowMovingInventory(@RequestParam(defaultValue = "30") Integer thresholdDays) {
        return inventoryMonitorService.identifySlowMovingInventory(thresholdDays);
    }
}