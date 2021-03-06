package tech.muyi.log;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import tech.muyi.common.constant.enumtype.DeviceEnum;
import tech.muyi.log.annotation.MyTraceIdCreate;
import tech.muyi.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;

@Data
public class LogRecordContext {

    private static final InheritableThreadLocal<Stack<Map<String, Object>>> variableMapStack = new InheritableThreadLocal<>();

    /**
     * 保存变量信息到 stack 中
     * @param name
     * @param value
     */
    public static void putVariable(String name, Object value){
        if (variableMapStack.get() == null){
            Stack<Map<String, Object>> stack = new Stack<>();
            variableMapStack.set(stack);
        }
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        if (mapStack.size() == 0){
            variableMapStack.get().push(new HashMap<>());
        }
        variableMapStack.get().peek().put(name, value);
    }

    public static Object getVariable(String key){
        Map<String, Object> variableMap = variableMapStack.get().peek();
        return variableMap.get(key);

    }

    public static  Map<String, Object> getVariables(){
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        return mapStack.peek();
    }

    public static void clear(){
        if (variableMapStack.get() != null){
            variableMapStack.get().pop();
        }
    }

    /**
     * 日志适用方不需要使用到这个方法
     * 每进入一个方法初始化一个 span 放入到 stack 中,方法执行完 pop 掉这个 span
     */
    public static void putEmptySpan(){
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        if (mapStack == null){
            Stack<Map<String, Object>> stack = new Stack<>();
            variableMapStack.set(stack);
        }
        variableMapStack.get().push(new HashMap<>());
    }

    public static void onceRestLogRecord(HttpServletRequest request){

        // 判断前端是否传入全链路ID信息
        String traceId = request.getHeader(LogConstant.TRACE_ID);
        if(StringUtils.isEmpty(traceId)) {
            // 设置traceId
            traceId = LogRecordUtil.getInitTraceId();
        }
        MDC.put(LogConstant.TRACE_ID,traceId);

        // 设置SpanId（正数留给服务端，0留给客户端）
        String spanId = "1";
        MDC.put(LogConstant.SPAN_ID,spanId);

        String logicId = "1";
        MDC.put(LogConstant.LOGIC_ID,logicId);

        // 设置ip
        String ipAddress = IpUtil.getIpAddr(request);
        MDC.put(LogConstant.IP_MDC,ipAddress);

        // 设置设备类型
        String deviceType = request.getHeader(LogConstant.DEVICE_TYPE)==null? DeviceEnum.OTHER.getDesc():request.getHeader(LogConstant.DEVICE_TYPE);
        MDC.put(LogConstant.DEVICE_TYPE,deviceType);
    }

    public static void onceRestLogRecord(MyTraceIdCreate myTraceIdCreate){

        // 设置traceId
        String traceId = LogRecordUtil.getInitTraceId();
        MDC.put(LogConstant.TRACE_ID,traceId);

        // 设置SpanId
        String spanId = "1";
        MDC.put(LogConstant.SPAN_ID,spanId);

        String logicId = "1";
        MDC.put(LogConstant.LOGIC_ID,logicId);

        // 设置ip
        MDC.put(LogConstant.IP_MDC,myTraceIdCreate.ip());

        // 设置设备类型
        MDC.put(LogConstant.DEVICE_TYPE,myTraceIdCreate.deviceType());
    }


    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }

            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }


    /**
     * 清理当前线程链路信息
     */
    public static void clearMDC() {
        MDC.clear();
    }


}
