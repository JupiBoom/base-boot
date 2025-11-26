package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.Product;

/**
 * 产品仓库接口
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 根据产品编码查询产品
     * @param productCode 产品编码
     * @return 产品信息
     */
    Product findByProductCode(String productCode);
}
