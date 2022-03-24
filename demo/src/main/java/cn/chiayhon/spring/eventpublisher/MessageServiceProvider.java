package cn.chiayhon.spring.eventpublisher;

import cn.chiayhon.spring.eventpublisher.dto.MessageData;
import cn.chiayhon.spring.eventpublisher.dto.MessageType;
import cn.chiayhon.spring.eventpublisher.event.RegisterSuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageServiceProvider {

    private final ApplicationContext context;

    @Autowired
    public MessageServiceProvider(ApplicationContext context) {
        this.context = context;
    }

    @Async
    public void sendMessage(MessageData data) {
        final MessageType type = data.getType();
        log.info("发布信息事件：" + type.getDesc());
        ApplicationEvent event;
        switch (type) {
            case REGISTER_SUCCESS:
                event = new RegisterSuccessEvent(data, type, data.getUser());
                break;
            default:
                throw new RuntimeException("不支持的事件");
        }
        context.publishEvent(event);
    }
}
