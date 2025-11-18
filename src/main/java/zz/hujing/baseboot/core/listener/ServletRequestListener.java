package zz.hujing.baseboot.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import java.util.Optional;

/**
 * 监听处理请求完毕事件
 **/
@Component
public class ServletRequestListener implements ApplicationListener<ServletRequestHandledEvent> {

    private static final Logger log = LoggerFactory.getLogger(ServletRequestListener.class);

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        log.debug("request -> {}", event.getRequestUrl());
        log.debug("execute method -> {}", event.getMethod());
        log.debug("spend time -> {} ms", event.getProcessingTimeMillis());
        log.debug("exception msg -> {}", Optional.ofNullable(event.getFailureCause()).map(Throwable::getMessage).orElse(null));
    }
}