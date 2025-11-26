package zz.hujing.baseboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zz.hujing.baseboot.domain.DynamicConfig;
import zz.hujing.baseboot.service.DynamicConfigService;
import zz.hujing.baseboot.core.result.CommonResult;

@RestController
@RequestMapping("/api/config")
public class DynamicConfigController {

    @Autowired
    private DynamicConfigService dynamicConfigService;

    /**
     * 获取当前动态配置
     * @return 配置信息
     */
    @GetMapping("/current")
    public CommonResult<DynamicConfig> getCurrentConfig() {
        DynamicConfig config = dynamicConfigService.getCurrentConfig();
        return CommonResult.success(config);
    }

    /**
     * 更新动态配置
     * @param config 新的配置信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public CommonResult<Void> updateConfig(@RequestBody DynamicConfig config) {
        dynamicConfigService.updateConfig(config);
        return CommonResult.success();
    }
}
