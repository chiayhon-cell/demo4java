package cn.chiayhon.spring.eventpublisher.dto;

import lombok.Getter;

@Getter
public enum MessageTemplate {

    FORMAT_REGISTER_SUCCESS("尊敬的%s：恭喜成为%s的一员，感谢您的注册！");

    private final String format;

    MessageTemplate(String format) {
        this.format = format;
    }
}
