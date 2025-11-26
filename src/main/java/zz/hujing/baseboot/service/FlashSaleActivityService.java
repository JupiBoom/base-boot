package zz.hujing.baseboot.service;

import zz.hujing.baseboot.domain.FlashSaleActivity;
import zz.hujing.baseboot.domain.FlashSaleActivityProduct;

import java.util.List;
import java.util.Map;

/**
 * 限时折扣活动服务接口
 */
public interface FlashSaleActivityService {
    /**
     * 根据ID查询活动
     * @param id 活动ID
     * @return 活动信息
     */
    FlashSaleActivity findById(Long id);

    /**
     * 分页查询活动
     * @param params 查询参数
     * @return 活动列表
     */
    List<FlashSaleActivity> findByPage(Map<String, Object> params);

    /**
     * 保存活动
     * @param activity 活动信息
     * @return 保存后的活动信息
     */
    FlashSaleActivity save(FlashSaleActivity activity);

    /**
     * 更新活动
     * @param activity 活动信息
     * @return 更新后的活动信息
     */
    FlashSaleActivity update(FlashSaleActivity activity);

    /**
     * 删除活动
     * @param id 活动ID
     */
    void delete(Long id);

    /**
     * 审核活动
     * @param id 活动ID
     * @param status 审核状态
     * @param remark 审核备注
     */
    void audit(Long id, Integer status, String remark);

    /**
     * 查询正在进行中的活动
     * @return 活动列表
     */
    List<FlashSaleActivity> findCurrentActivities();

    /**
     * 查询即将开始的活动
     * @return 活动列表
     */
    List<FlashSaleActivity> findUpcomingActivities();

    /**
     * 查询活动商品
     * @param activityId 活动ID
     * @return 活动商品列表
     */
    List<FlashSaleActivityProduct> findActivityProducts(Long activityId);

    /**
     * 保存活动商品
     * @param activityProduct 活动商品信息
     * @return 保存后的活动商品信息
     */
    FlashSaleActivityProduct saveActivityProduct(FlashSaleActivityProduct activityProduct);

    /**
     * 删除活动商品
     * @param id 活动商品ID
     */
    void deleteActivityProduct(Long id);

    /**
     * 扣减活动商品库存
     * @param activityProductId 活动商品ID
     * @param quantity 扣减数量
     * @return 是否扣减成功
     */
    boolean deductActivityProductStock(Long activityProductId, Integer quantity);
}
