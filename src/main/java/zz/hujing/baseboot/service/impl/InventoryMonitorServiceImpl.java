package zz.hujing.baseboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.alert.AlertRecord;
import zz.hujing.baseboot.domain.alert.AlertRule;
import zz.hujing.baseboot.domain.inventory.Inventory;
import zz.hujing.baseboot.domain.product.Product;
import zz.hujing.baseboot.repository.AlertRecordRepository;
import zz.hujing.baseboot.repository.AlertRuleRepository;
import zz.hujing.baseboot.repository.InventoryRepository;
import zz.hujing.baseboot.repository.ProductRepository;
import zz.hujing.baseboot.service.InventoryMonitorService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryMonitorServiceImpl implements InventoryMonitorService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final AlertRuleRepository alertRuleRepository;
    private final AlertRecordRepository alertRecordRepository;

    @Override
    public List<AlertRecord> monitorInventoryLevels() {
        List<AlertRecord> alerts = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        List<AlertRule> rules = alertRuleRepository.findByRuleTypeAndIsEnabledTrue("INVENTORY_LEVEL");

        for (Product product : products) {
            Inventory inventory = inventoryRepository.findByProductId(product.getId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + product.getId()));

            // 检查库存水位
            if (inventory.getAvailableStock() < product.getSafetyStock()) {
                AlertRule rule = rules.stream()
                        .filter(r -> r.getThresholdValue() == product.getSafetyStock().doubleValue())
                        .findFirst()
                        .orElse(null);

                if (rule != null) {
                    AlertRecord alert = new AlertRecord();
                    alert.setProductId(product.getId());
                    alert.setAlertType("INVENTORY_LEVEL");
                    alert.setAlertLevel(rule.getAlertLevel());
                    alert.setAlertMessage(String.format("产品%s库存不足，当前库存：%d，安全库存：%d",
                            product.getProductName(), inventory.getAvailableStock(), product.getSafetyStock()));
                    alert.setCurrentValue(inventory.getAvailableStock().doubleValue());
                    alert.setThresholdValue(product.getSafetyStock().doubleValue());
                    alerts.add(alert);
                }
            }
        }

        // 保存预警记录
        if (!alerts.isEmpty()) {
            alertRecordRepository.saveAll(alerts);
        }

        return alerts;
    }

    @Override
    public Double calculateInventoryTurnover(Long productId, Integer periodDays) {
        // 简化实现：库存周转率 = 销售成本 / 平均库存
        // 这里用销售数量代替销售成本，平均库存用当前库存代替
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        // 这里应该查询periodDays内的销售数量
        // 为了简化，暂时返回一个模拟值
        return Math.random() * 10; // 实际应该根据销售数据计算
    }

    @Override
    public Map<String, Object> analyzeInventoryAge(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        Map<String, Object> result = new HashMap<>();
        if (inventory.getLastSaleTime() != null) {
            long daysSinceLastSale = ChronoUnit.DAYS.between(inventory.getLastSaleTime(), LocalDateTime.now());
            result.put("daysSinceLastSale", daysSinceLastSale);
            result.put("inventoryAgeStatus", daysSinceLastSale > 30 ? "呆滞" : "正常");
        } else {
            result.put("daysSinceLastSale", null);
            result.put("inventoryAgeStatus", "从未销售");
        }

        return result;
    }

    @Override
    public List<Inventory> identifySlowMovingInventory(Integer thresholdDays) {
        List<Inventory> allInventory = inventoryRepository.findAll();
        List<Inventory> slowMoving = new ArrayList<>();

        for (Inventory inventory : allInventory) {
            if (inventory.getLastSaleTime() != null) {
                long daysSinceLastSale = ChronoUnit.DAYS.between(inventory.getLastSaleTime(), LocalDateTime.now());
                if (daysSinceLastSale > thresholdDays) {
                    slowMoving.add(inventory);
                }
            } else {
                // 从未销售的产品也视为呆滞库存
                slowMoving.add(inventory);
            }
        }

        return slowMoving;
    }
}