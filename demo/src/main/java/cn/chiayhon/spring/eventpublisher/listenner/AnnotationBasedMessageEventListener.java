package cn.chiayhon.spring.eventpublisher.listenner;

import cn.chiayhon.pojo.User;
import cn.chiayhon.spring.eventpublisher.dto.MessageType;
import cn.chiayhon.spring.eventpublisher.event.MessageEvent;
import cn.chiayhon.spring.eventpublisher.event.RegisterSuccessEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AnnotationBasedMessageEventListener {

    //    @EventListener(MessageEvent.class)
    @SneakyThrows
    public void listen(MessageEvent event) {
        String message;
        final MessageType type = event.getType();
        log.info("成功接受信息事件:" + type.getDesc());
        switch (type) {
            case REGISTER_SUCCESS:
                RegisterSuccessEvent originEvent = (RegisterSuccessEvent) event;
                final User user = originEvent.getUser();
                final String format = type.getTemplate().getFormat();
                final String username = user.getUsername();
                final String companyName = user.getCompany().getName();
                message = String.format(format, username, companyName);
                break;
            default:
                throw new RuntimeException();
        }

        log.info("发送信息");

        for (int i = 5; i > 0; i--) {
            TimeUnit.SECONDS.sleep(1);
            log.info("预计信息到达剩余时间:" + i);
        }

        log.info("收到信息：" + message);
    }
}

