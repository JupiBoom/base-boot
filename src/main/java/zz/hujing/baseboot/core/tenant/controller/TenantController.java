package zz.hujing.baseboot.core.tenant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zz.hujing.baseboot.core.result.Result;
import zz.hujing.baseboot.core.result.ResultGenerator;
import zz.hujing.baseboot.core.tenant.model.Tenant;
import zz.hujing.baseboot.core.tenant.service.TenantService;

/**
 * 租户控制器
 * @author hujing
 */
@RestController
@RequestMapping("/api/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    /**
     * 租户注册
     * @param tenant 租户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result registerTenant(@RequestBody Tenant tenant) {
        boolean result = tenantService.registerTenant(tenant);
        if (result) {
            return ResultGenerator.genSuccessResult("租户注册成功");
        } else {
            return ResultGenerator.genFailResult("租户注册失败");
        }
    }
}