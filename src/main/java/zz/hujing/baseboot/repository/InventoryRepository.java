package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.Inventory;

/**
 * 库存仓库接口
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * 根据产品ID查询库存
     * @param productId 产品ID
     * @return 库存信息
     */
    Inventory findByProductId(Long productId);
}
