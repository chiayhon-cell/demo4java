package cn.chiayhon.spring.eventpublisher.event;

import cn.chiayhon.pojo.User;
import cn.chiayhon.spring.eventpublisher.dto.MessageType;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class RegisterSuccessEvent extends MessageEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final User user;

    public RegisterSuccessEvent(Object source, MessageType type, User user) {
        super(source, type);
        this.user = user;
    }
}
