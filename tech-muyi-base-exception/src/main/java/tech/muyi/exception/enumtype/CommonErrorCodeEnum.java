package tech.muyi.exception.enumtype;


import tech.muyi.exception.BaseErrorInfoInterface;

/**
 * @Author: muyi
 * @Date: 2021/1/3 21:38
 */
public enum CommonErrorCodeEnum implements BaseErrorInfoInterface {
    /***************************************************************************************************************
     *                通用状态码:
     *                4XX：前端发生错误
     *                5XX：服务端发生错误
     * *************************************************************************************************************/
    SUCCESS("200", "成功!"),
    BAD_REQUEST("400","请求有误!"),
    AUTHENTICATION("401","请求认证类型错误!"),
    SIGNATURE_ERROR("402","请求签名错误!"),
    AUTHORIZATION("403","请求授权错误!"),
    NOT_FOUND("404", "请求访问数据不存在!"),
    REQUEST_ENTITY_TOO_LARGE("414", "请求消息体过长!"),
    UNSUPPORTED_MEDIA_TYPE("415", "服务器端不支持客户端请求首部Content-Type里指定的数据格式!"),
    MISDIRECTED_REQUEST("421", "请求处理方错误!"),

    INTERNAL_SERVER_ERROR("500", "服务器内部错误!"),
    NOT_IMPLEMENTED("501", "服务器不支持实现请求所需要的功能!"),
    BAD_GATEWAY("502", "代理服务器无法获取到合法资源!"),
    SERVER_BUSY("503","服务器正忙，请稍后再试!"),
    GATEWAY_TIMEOUT("504","代理服务器无法及时的从上游获得响应!"),
    HTTP_VERSON_NOT_SUPPORTED("505","请求使用的HTTP协议版本不支持!"),

    UNKNOWN_EXCEPTION( "600", "未知异常"),



    INVALID_PARAM( "10000", "参数不合法"),
    DB_EXCEPTION("10001", "数据库异常"),
    NULL_POINTER("10002", "空指针异常"),
    FLOW_EXCEPTION("10003", "限流"),
    SERIALIZATION_FAIL("10004","序列化失败"),


    //    mq类型错误 11100~11099
    MQ_PRODUCT_EXCEPTION("11100","MQ生产者启动异常"),
    MQ_CONSUMER_EXCEPTION("11101","MQ消费者启动异常"),
    MQ_ONE_WAY_SEND_FAIL_EXCEPTION("11102","MQ单向消息发送异常"),
    MQ_ASNY_SEND_FAIL_EXCEPTION("11103","MQ异步消息发送异常"),
    MQ_SYNC_SEND_FAIL_EXCEPTION("11104","MQ同步消息发送异常"),
    MQ_DELAY_SEND_FAIL_EXCEPTION("11105","MQ延时消息发送异常")
    ;

    /** 错误码 */
    private String resultCode;

    /** 错误描述 */
    private String resultMsg;

    CommonErrorCodeEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
