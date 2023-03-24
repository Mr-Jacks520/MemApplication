package pers.hence.memapplication.constant;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/23 17:37
 * @description 状态码常量
 */
public class StatusCode {

    /**
     * 请求成功处理
     */
    public static final Integer OK = 200;

    /**
     * 通用请求错误
     */
    public static final Integer ERROR = 400;

    /**
     * 未认证
     */
    public static final Integer NOT_AUTH = 401;

    /**
     * 拒绝访问
     */
    public static final Integer REFUSE = 403;

    /**
     * 资源不存在
     */
    public static final Integer NOT_FOUND = 404;

    /**
     * 内部错误
     */
    public static final Integer INTERNAL_ERROR = 500;

    /**
     * 服务停机
     */
    public static final Integer BAD_GATEWAY = 503;
}
