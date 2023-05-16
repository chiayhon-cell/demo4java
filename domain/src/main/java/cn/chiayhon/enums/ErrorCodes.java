package cn.chiayhon.enums;

import lombok.Getter;

/**
 * 错误码枚举
 */
@Getter
public enum ErrorCodes {

    UNKNOWN(10000, "未知异常"),

    ILLEGAL_ARGUMENT(10001, "非法参数");

    ErrorCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误信息
     */
    private final String message;
}
