package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.FlashSaleActivityProduct;

import java.util.List;

/**
 * 限时折扣活动商品Repository
 */
@Repository
public interface FlashSaleActivityProductRepository extends JpaRepository<FlashSaleActivityProduct, Long>, JpaSpecificationExecutor<FlashSaleActivityProduct> {
    /**
     * 根据活动ID查询活动商品
     * @param activityId 活动ID
     * @return 活动商品列表
     */
    List<FlashSaleActivityProduct> findByActivityIdOrderByCreateTimeDesc(Long activityId);

    /**
     * 根据活动ID和商品ID查询
     * @param activityId 活动ID
     * @param productId 商品ID
     * @return 活动商品信息
     */
    FlashSaleActivityProduct findByActivityIdAndProductId(Long activityId, Long productId);
}
