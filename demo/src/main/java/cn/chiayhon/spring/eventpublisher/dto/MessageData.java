package cn.chiayhon.spring.eventpublisher.dto;

import cn.chiayhon.pojo.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageData {

    private MessageType type; // 信息类型

    private User user;

}
