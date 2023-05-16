package cn.chiayhon.exception;


import cn.chiayhon.enums.ErrorCodes;

import java.util.Optional;

/**
 * 业务异常，从业务层代码抛出
 */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected Object data;

    protected final Integer code;

    protected static final Integer CODE_UNKNOWN = ErrorCodes.UNKNOWN.getCode();

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public BizException(Integer code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.code = Optional.ofNullable(code).orElse(CODE_UNKNOWN);
        this.data = data;
    }

    public BizException(Integer code, String message, Throwable cause) {
        this(code, message, null, cause);
    }

    public BizException() {
        this(null, null, null, null);
    }


    public BizException(String message) {
        this(null, message, null, null);
    }

    public BizException(Throwable cause) {
        this(null, null, null, cause);
    }

    public BizException(Integer code, String message) {
        this(code, message, null, null);
    }


    public BizException(String message, Throwable cause) {
        this(null, message, null, cause);
    }

    public BizException(ErrorCodes codeType) {
        this(codeType.getCode(), codeType.getMessage(), null, null);
    }

    public BizException(ErrorCodes codeType, Throwable cause) {
        this(codeType.getCode(), codeType.getMessage(), null, cause);
    }

    public BizException(ErrorCodes codeType, Object data, Throwable cause) {
        this(codeType.getCode(), codeType.getMessage(), data, cause);
    }

}
