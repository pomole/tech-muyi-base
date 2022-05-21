package tech.muyi.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: muyi
 * @Date: 2021/1/30 22:49
 */
public class MyMqConsumer {
    private static Logger logger = LoggerFactory.getLogger(MyMqConsumer.class);
    public MyMqConsumer(String namesrvAddr, String groupName, String topic, String tag, String instanceName, MyMqMessageListener myMqMessageListener) throws MQClientException {
        logger.info("消费者订阅关系确定：namesrvAddr:{}-groupName:{}-groupName:{}-groupName:{}-instanceName:{}", namesrvAddr,groupName,groupName,groupName,instanceName);
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        if (null != instanceName && !"".equals(instanceName)) {
            consumer.setInstanceName(instanceName);
        }

        consumer.subscribe(topic, tag);
        MyMqListener myMqListener = new MyMqListener();
        myMqListener.setMyMqMessageListener(myMqMessageListener);
        consumer.registerMessageListener(myMqListener);
        consumer.start();
        logger.info("消费者订阅关系确定完成");
    }
}
