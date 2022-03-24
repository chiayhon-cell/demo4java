package cn.chiayhon.spring.eventpublisher.event;

import cn.chiayhon.spring.eventpublisher.dto.MessageType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public abstract class MessageEvent extends ApplicationEvent {

    private MessageType type;

    protected MessageEvent(Object source, MessageType type) {
        super(source);
        this.type = type;
    }
}
