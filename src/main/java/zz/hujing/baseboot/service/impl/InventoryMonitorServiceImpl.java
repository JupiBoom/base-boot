package zz.hujing.baseboot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zz.hujing.baseboot.domain.AlertRecord;
import zz.hujing.baseboot.domain.Inventory;
import zz.hujing.baseboot.domain.Product;
import zz.hujing.baseboot.domain.SalesRecord;
import zz.hujing.baseboot.repository.AlertRecordRepository;
import zz.hujing.baseboot.repository.InventoryRepository;
import zz.hujing.baseboot.repository.ProductRepository;
import zz.hujing.baseboot.repository.SalesRecordRepository;
import zz.hujing.baseboot.service.InventoryMonitorService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存监控服务实现类
 */
@Slf4j
@Service
public class InventoryMonitorServiceImpl implements InventoryMonitorService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private SalesRecordRepository salesRecordRepository;

    @Autowired
    private AlertRecordRepository alertRecordRepository;

    /**
     * 监控所有产品的库存水位
     * @return 预警记录列表
     */
    @Override
    public List<AlertRecord> monitorInventoryLevel() {
        List<Product> products = productRepository.findAll();
        List<AlertRecord> alertRecords = new ArrayList<>();

        for (Product product : products) {
            AlertRecord alertRecord = monitorInventoryLevelByProductId(product.getId());
            if (alertRecord != null) {
                alertRecords.add(alertRecord);
            }
        }

        return alertRecords;
    }

    /**
     * 监控单个产品的库存水位
     * @param productId 产品ID
     * @return 预警记录
     */
    @Override
    public AlertRecord monitorInventoryLevelByProductId(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            log.error("产品不存在，productId: {}", productId);
            return null;
        }

        Inventory inventory = inventoryRepository.findByProductId(productId);
        if (inventory == null || inventory.getCurrentStock() == null) {
            log.error("库存信息不存在，productId: {}", productId);
            return null;
        }

        int currentStock = inventory.getCurrentStock();
        int safetyStock = product.getSafetyStock();

        if (currentStock <= safetyStock) {
            AlertRecord alertRecord = new AlertRecord();
            alertRecord.setProductId(productId);
            alertRecord.setAlertType(1); // 库存不足

            if (currentStock <= safetyStock * 0.3) {
                alertRecord.setAlertLevel(3); // 紧急
            } else if (currentStock <= safetyStock * 0.6) {
                alertRecord.setAlertLevel(2); // 警告
            } else {
                alertRecord.setAlertLevel(1); // 提醒
            }

            alertRecord.setAlertContent(String.format("产品%s当前库存为%d，已低于安全库存阈值%d",
                    product.getProductName(), currentStock, safetyStock));
            alertRecord.setHandleStatus(0); // 未处理
            alertRecord.setCreateTime(LocalDateTime.now());

            alertRecordRepository.save(alertRecord);
            log.info("生成库存不足预警，productId: {}, productName: {}", productId, product.getProductName());
            return alertRecord;
        }

        return null;
    }

    /**
     * 计算库存周转率
     * @param productId 产品ID
     * @param days 统计天数
     * @return 库存周转率
     */
    @Override
    public double calculateInventoryTurnover(Long productId, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        LocalDateTime endDate = LocalDateTime.now();

        // 查询指定时间段内的销售记录
        List<SalesRecord> salesRecords = salesRecordRepository.findByProductIdAndSalesDateBetween(productId, startDate, endDate);
        if (salesRecords.isEmpty()) {
            return 0.0;
        }

        // 计算销售总量
        int totalSales = salesRecords.stream().mapToInt(SalesRecord::getSalesQuantity).sum();

        // 查询当前库存
        Inventory inventory = inventoryRepository.findByProductId(productId);
        if (inventory == null || inventory.getCurrentStock() == null) {
            return 0.0;
        }

        int currentStock = inventory.getCurrentStock();

        // 计算平均库存
        double averageStock = currentStock / 2.0;

        // 计算库存周转率
        if (averageStock == 0) {
            return 0.0;
        }

        return totalSales / averageStock;
    }

    /**
     * 监控库存周转率
     * @param days 统计天数
     * @return 预警记录列表
     */
    @Override
    public List<AlertRecord> monitorInventoryTurnover(int days) {
        List<Product> products = productRepository.findAll();
        List<AlertRecord> alertRecords = new ArrayList<>();

        for (Product product : products) {
            double turnover = calculateInventoryTurnover(product.getId(), days);

            // 假设库存周转率低于0.5时需要预警
            if (turnover < 0.5) {
                AlertRecord alertRecord = new AlertRecord();
                alertRecord.setProductId(product.getId());
                alertRecord.setAlertType(2); // 库存周转率异常
                alertRecord.setAlertLevel(2); // 警告
                alertRecord.setAlertContent(String.format("产品%s近%d天的库存周转率为%.2f，低于正常水平",
                        product.getProductName(), days, turnover));
                alertRecord.setHandleStatus(0); // 未处理
                alertRecord.setCreateTime(LocalDateTime.now());

                alertRecordRepository.save(alertRecord);
                alertRecords.add(alertRecord);
                log.info("生成库存周转率异常预警，productId: {}, productName: {}", product.getId(), product.getProductName());
            }
        }

        return alertRecords;
    }

    /**
     * 分析库龄
     * @param productId 产品ID
     * @return 库龄（天）
     */
    @Override
    public int analyzeInventoryAge(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId);
        if (inventory == null || inventory.getLastInTime() == null) {
            return 0;
        }

        LocalDateTime lastInTime = inventory.getLastInTime();
        LocalDateTime now = LocalDateTime.now();

        return (int) ChronoUnit.DAYS.between(lastInTime, now);
    }

    /**
     * 识别呆滞库存
     * @param maxAge 最大允许库龄（天）
     * @return 呆滞库存产品列表
     */
    @Override
    public List<Product> identifySlowMovingInventory(int maxAge) {
        List<Product> products = productRepository.findAll();
        List<Product> slowMovingProducts = new ArrayList<>();

        for (Product product : products) {
            int inventoryAge = analyzeInventoryAge(product.getId());
            if (inventoryAge > maxAge) {
                slowMovingProducts.add(product);

                // 生成呆滞库存预警
                AlertRecord alertRecord = new AlertRecord();
                alertRecord.setProductId(product.getId());
                alertRecord.setAlertType(3); // 呆滞库存
                alertRecord.setAlertLevel(1); // 提醒
                alertRecord.setAlertContent(String.format("产品%s的库龄为%d天，已超过最大允许库龄%d天",
                        product.getProductName(), inventoryAge, maxAge));
                alertRecord.setHandleStatus(0); // 未处理
                alertRecord.setCreateTime(LocalDateTime.now());

                alertRecordRepository.save(alertRecord);
                log.info("生成呆滞库存预警，productId: {}, productName: {}", product.getId(), product.getProductName());
            }
        }

        return slowMovingProducts;
    }
}
