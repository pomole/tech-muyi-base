package dubbo;


import com.alibaba.dubbo.rpc.RpcContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import tech.muyi.log.LogConstant;
import tech.muyi.log.LogRecordUtil;
import tech.muyi.log.annotation.MyTraceIdCreate;
import tech.muyi.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;

@Data
public class LogRecordContext {

    /**
     * 消费者传入链路信息（调用方）
     */
    public static void onceRpcConsumerLogRecord(){
        RpcContext.getContext().setAttachment(LogConstant.TRACE_ID, LogRecordUtil.getTraceId());
        RpcContext.getContext().setAttachment(LogConstant.SPAN_ID, LogRecordUtil.getSpanId());
        RpcContext.getContext().setAttachment(LogConstant.LOGIC_ID, LogRecordUtil.incrLogicId(LogRecordUtil.getLogicId()));
    }

    /**
     * 生产者获取链路信息（被调用方）
     */
    public static void onceRpcProviderLogRecord(){
        String traceId = RpcContext.getContext().getAttachment(LogConstant.TRACE_ID);
        MDC.put(LogConstant.TRACE_ID,traceId);

        String logicId = RpcContext.getContext().getAttachment(LogConstant.LOGIC_ID);
        MDC.put(LogConstant.LOGIC_ID,"0");

        String spanId = RpcContext.getContext().getAttachment(LogConstant.SPAN_ID);
        MDC.put(LogConstant.SPAN_ID,LogRecordUtil.getNewSpanId(spanId,logicId));

    }


    /**
     * 清理当前线程链路信息
     */
    public static void clearMDC() {
        MDC.clear();
    }
}
