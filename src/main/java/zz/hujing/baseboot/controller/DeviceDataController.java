package zz.hujing.baseboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zz.hujing.baseboot.domain.DeviceData;
import zz.hujing.baseboot.service.DeviceDataService;
import zz.hujing.baseboot.core.result.CommonResult;

@RestController
@RequestMapping("/api/device")
public class DeviceDataController {

    @Autowired
    private DeviceDataService deviceDataService;

    @PostMapping("/data")
    public CommonResult<Void> receiveDeviceData(@RequestBody DeviceData deviceData) {
        deviceDataService.processDeviceData(deviceData);
        return CommonResult.success();
    }
}