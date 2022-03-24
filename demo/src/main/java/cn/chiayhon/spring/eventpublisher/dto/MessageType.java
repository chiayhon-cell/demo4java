package cn.chiayhon.spring.eventpublisher.dto;

import lombok.Getter;

import static cn.chiayhon.spring.eventpublisher.dto.MessageTemplate.FORMAT_REGISTER_SUCCESS;

@Getter
public enum MessageType {

    REGISTER_SUCCESS("注册成功", FORMAT_REGISTER_SUCCESS);

    private final String desc; // 信息类型描述

    private final MessageTemplate template; // 信息模板

    MessageType(String desc, MessageTemplate template) {
        this.desc = desc;
        this.template = template;
    }
}
