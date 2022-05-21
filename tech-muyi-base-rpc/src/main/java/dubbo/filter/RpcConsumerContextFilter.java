package dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import dubbo.LogRecordContext;

/**
 * description: ConsumerContextFilter
 * date: 2021/11/14 2:15
 * author: muyi
 * version: 1.0
 */
@Activate(group = Constants.CONSUMER, order = -10000)
public class RpcConsumerContextFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            if (RpcContext.getContext().isConsumerSide()) {
                // 消费者
                LogRecordContext.onceRpcConsumerLogRecord();
            }
            return invoker.invoke(invocation);
        }finally {
            LogRecordContext.clearMDC();
        }
    }

}