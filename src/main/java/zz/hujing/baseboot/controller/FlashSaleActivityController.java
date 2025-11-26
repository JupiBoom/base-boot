package zz.hujing.baseboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.domain.FlashSaleActivity;
import zz.hujing.baseboot.domain.FlashSaleActivityProduct;
import zz.hujing.baseboot.service.FlashSaleActivityService;

import java.util.List;
import java.util.Map;

/**
 * 限时折扣活动管理Controller
 */
@RestController
@RequestMapping("/api/flashSaleActivity")
public class FlashSaleActivityController {

    @Autowired
    private FlashSaleActivityService flashSaleActivityService;

    /**
     * 根据ID查询限时折扣活动
     */
    @GetMapping("/{id}")
    public FlashSaleActivity findById(@PathVariable Long id) {
        return flashSaleActivityService.findById(id);
    }

    /**
     * 分页查询限时折扣活动
     */
    @GetMapping("/page")
    public List<FlashSaleActivity> findByPage(@RequestParam Map<String, Object> params) {
        return flashSaleActivityService.findByPage(params);
    }

    /**
     * 保存限时折扣活动
     */
    @PostMapping
    public FlashSaleActivity save(@RequestBody FlashSaleActivity activity) {
        return flashSaleActivityService.save(activity);
    }

    /**
     * 更新限时折扣活动
     */
    @PutMapping
    public FlashSaleActivity update(@RequestBody FlashSaleActivity activity) {
        return flashSaleActivityService.update(activity);
    }

    /**
     * 删除限时折扣活动
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        flashSaleActivityService.delete(id);
    }

    /**
     * 审核限时折扣活动
     */
    @PutMapping("/audit/{id}")
    public void audit(@PathVariable Long id, @RequestParam Integer status, @RequestParam(required = false) String remark) {
        flashSaleActivityService.audit(id, status, remark);
    }

    /**
     * 查询进行中的限时折扣活动
     */
    @GetMapping("/active")
    public List<FlashSaleActivity> findActiveActivities() {
        return flashSaleActivityService.findCurrentActivities();
    }

    /**
     * 查询即将开始的限时折扣活动
     */
    @GetMapping("/upcoming")
    public List<FlashSaleActivity> findUpcomingActivities() {
        return flashSaleActivityService.findUpcomingActivities();
    }

    /**
     * 查询活动商品
     */
    @GetMapping("/products/{activityId}")
    public List<FlashSaleActivityProduct> findActivityProducts(@PathVariable Long activityId) {
        return flashSaleActivityService.findActivityProducts(activityId);
    }

    /**
     * 保存活动商品
     */
    @PostMapping("/product")
    public FlashSaleActivityProduct saveActivityProduct(@RequestBody FlashSaleActivityProduct activityProduct) {
        return flashSaleActivityService.saveActivityProduct(activityProduct);
    }

    /**
     * 删除活动商品
     */
    @DeleteMapping("/product/{id}")
    public void deleteActivityProduct(@PathVariable Long id) {
        flashSaleActivityService.deleteActivityProduct(id);
    }

    /**
     * 扣减活动商品库存
     */
    @PutMapping("/product/decreaseStock/{activityProductId}")
    public void decreaseStock(@PathVariable Long activityProductId, @RequestParam Integer quantity) {
        flashSaleActivityService.deductActivityProductStock(activityProductId, quantity);
    }
}
