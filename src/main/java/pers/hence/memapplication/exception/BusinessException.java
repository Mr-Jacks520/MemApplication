package pers.hence.memapplication.exception;

/**
 * 自定义异常类
 *
 * @author yupi
 */
public class BusinessException extends RuntimeException {

    private final int code;

    private final String message;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
