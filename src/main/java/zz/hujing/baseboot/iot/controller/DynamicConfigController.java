package zz.hujing.baseboot.iot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zz.hujing.baseboot.iot.config.DynamicConfig;

@Slf4j
@RestController
@RequestMapping("/api/config")
public class DynamicConfigController {

    @Autowired
    private DynamicConfig dynamicConfig;

    @GetMapping
    public ResponseEntity<DynamicConfig> getDynamicConfig() {
        try {
            return ResponseEntity.ok(dynamicConfig);
        } catch (Exception e) {
            log.error("Failed to get dynamic config", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<DynamicConfig> updateDynamicConfig(@RequestBody DynamicConfig newConfig) {
        try {
            // 更新配置
            dynamicConfig.setCollectionFrequency(newConfig.getCollectionFrequency());
            dynamicConfig.setAnomalyThreshold(newConfig.getAnomalyThreshold());
            dynamicConfig.setAlertCooldown(newConfig.getAlertCooldown());
            dynamicConfig.setOfflineThreshold(newConfig.getOfflineThreshold());

            log.info("Updated dynamic config: {}", dynamicConfig);
            return ResponseEntity.ok(dynamicConfig);
        } catch (Exception e) {
            log.error("Failed to update dynamic config: {}", newConfig, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}