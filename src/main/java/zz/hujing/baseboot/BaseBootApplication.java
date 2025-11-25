package zz.hujing.baseboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import zz.hujing.baseboot.core.result.CommonResult;


@SpringBootApplication
@EnableJpaAuditing
// @RestController
public class BaseBootApplication  {

    public static void main(String[] args) {
        SpringApplication.run(BaseBootApplication.class, args);
    }

    // @GetMapping("/echo")
    // public CommonResult<Void> echo() {
    //     return CommonResult.success();
    // }
}
