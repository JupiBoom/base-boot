package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.AlertRecord;
import zz.hujing.baseboot.domain.Inventory;
import zz.hujing.baseboot.domain.Product;

import java.util.List;

/**
 * 库存监控服务接口
 */
public interface InventoryMonitorService {

    /**
     * 监控所有产品的库存水位
     * @return 预警记录列表
     */
    List<AlertRecord> monitorInventoryLevel();

    /**
     * 监控单个产品的库存水位
     * @param productId 产品ID
     * @return 预警记录
     */
    AlertRecord monitorInventoryLevelByProductId(Long productId);

    /**
     * 计算库存周转率
     * @param productId 产品ID
     * @param days 统计天数
     * @return 库存周转率
     */
    double calculateInventoryTurnover(Long productId, int days);

    /**
     * 监控库存周转率
     * @param days 统计天数
     * @return 预警记录列表
     */
    List<AlertRecord> monitorInventoryTurnover(int days);

    /**
     * 分析库龄
     * @param productId 产品ID
     * @return 库龄（天）
     */
    int analyzeInventoryAge(Long productId);

    /**
     * 识别呆滞库存
     * @param maxAge 最大允许库龄（天）
     * @return 呆滞库存产品列表
     */
    List<Product> identifySlowMovingInventory(int maxAge);
}
