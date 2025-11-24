package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.alert.AlertRecord;
import zz.hujing.baseboot.domain.inventory.Inventory;

import java.util.List;
import java.util.Map;

public interface InventoryMonitorService {
    /**
     * 监控所有产品的库存水位
     */
    List<AlertRecord> monitorInventoryLevels();

    /**
     * 计算库存周转率
     * @param productId 产品ID
     * @param periodDays 计算周期（天）
     * @return 库存周转率
     */
    Double calculateInventoryTurnover(Long productId, Integer periodDays);

    /**
     * 分析库龄
     * @param productId 产品ID
     * @return 库龄分析结果
     */
    Map<String, Object> analyzeInventoryAge(Long productId);

    /**
     * 识别呆滞库存
     * @param thresholdDays 呆滞库存阈值（天）
     * @return 呆滞库存列表
     */
    List<Inventory> identifySlowMovingInventory(Integer thresholdDays);
}