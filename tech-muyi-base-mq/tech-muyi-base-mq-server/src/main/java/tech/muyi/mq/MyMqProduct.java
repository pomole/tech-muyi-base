package tech.muyi.mq;

import mq.exception.MqErrorCodeEnum;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.muyi.exception.MyException;
import tech.muyi.exception.enumtype.CommonErrorCodeEnum;
import tech.muyi.util.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: muyi
 * @Date: 2021/1/30 22:59
 */
public class MyMqProduct {
    private static Logger logger = LoggerFactory.getLogger(MyMqProduct.class);

    private DefaultMQProducer defaultMQProducer;

    public MyMqProduct(String namesrvAddr, String groupName) {
        this.defaultMQProducer = new DefaultMQProducer(groupName);
        this.defaultMQProducer.setNamesrvAddr(namesrvAddr);
        this.defaultMQProducer.setInstanceName(this.getClass().getSimpleName());

        try {
            this.defaultMQProducer.start();
        } catch (MQClientException e) {
            throw new MyException(MqErrorCodeEnum.MQ_PRODUCT_EXCEPTION);
        }
    }

    /**
     * εΊεε
     * @param topic
     * @param tag
     * @param key
     * @param body
     * @return
     */
    private Message convert(String topic, String tag, String key, Object body) {
        try {
            Message message = new Message(topic, tag, key, Base64.encodeBase64(JsonUtil.toJson(body, body.getClass()).getBytes("UTF-8")));
            return message;
        } catch (UnsupportedEncodingException e) {
            throw new MyException(CommonErrorCodeEnum.SERIALIZATION_FAIL, e);
        }
    }


    /**
     * εζ­₯εζ‘ειζΆζ―
     * @param topic
     * @param tag
     * @param key
     * @param body
     * @return
     */
    public SendResult send(String topic, String tag, String key, Object body) {
        Message message = null;
        try {
            message =  this.convert(topic, tag, key, body);
            return this.defaultMQProducer.send(message);
        } catch (MQBrokerException e){
            try {
                if ((new Integer(2)).equals(e.getResponseCode()) && message != null) {
                    int tryTime = 5;
                    return this.retrySend(message, tryTime);
                } else {
                    throw e;
                }
            } catch (Exception e1) {
                throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION, e1);
            }
        } catch( MQClientException | RemotingException | InterruptedException e2) {
            throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION, e2);
        }
    }

    /**
     * εζ­₯εζ‘ειε»ΆζΆζΆζ―
     * @param topic
     * @param tag
     * @param key
     * @param body
     * @param timeout
     * @return
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     */
    public SendResult send(String topic, String tag, String key, Object body,long timeout){
        Message message = null;
        try {
            message =  this.convert(topic, tag, key, body);
            return this.defaultMQProducer.send(message);
        } catch (MQBrokerException e){
            try {
                if ((new Integer(2)).equals(e.getResponseCode()) && message != null) {
                    int tryTime = 5;
                    return this.retrySend(message, tryTime,timeout );
                } else {
                    throw e;
                }
            } catch (Exception e1) {
                throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION, e1);
            }
        } catch( MQClientException | RemotingException | InterruptedException e2) {
            throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION, e2);
        }
    }


    /**
     * ζΉιεζ­₯ει
     * @param topic
     * @param tag
     * @param key
     * @param bodies
     * @return
     */
    public SendResult send(String topic, String tag, String key, List<?> bodies) {
        List<Message> messages = new ArrayList(bodies.size());
        bodies.forEach((body) -> {
            messages.add(this.convert(topic, tag, key, body));
        });
        try {
            return this.defaultMQProducer.send(messages);
        } catch (MQBrokerException e) {
            try {
                if ((new Integer(2)).equals(e.getResponseCode())) {
                    int tryTime = 5;
                    return this.retrySend(messages, tryTime);
                } else {
                    throw e;
                }
            } catch (Exception e1) {
                throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION, e1);
            }
        } catch (MQClientException | RemotingException | InterruptedException e2) {
            throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION, e2);
        }
    }

    /**
     * ζΉιεζ­₯ε»ΆζΆει
     * @param topic
     * @param tag
     * @param key
     * @param bodies
     * @param timeout
     * @return
     */
    public SendResult send(String topic, String tag, String key, List<?> bodies,long timeout) {
        List<Message> messages = new ArrayList(bodies.size());
        bodies.forEach((body) -> {
            messages.add(this.convert(topic, tag, key, body));
        });
        try {
            return this.defaultMQProducer.send(messages,timeout);
        } catch (MQBrokerException e) {
            try {
                if ((new Integer(2)).equals(e.getResponseCode())) {
                    int tryTime = 5;
                    return this.retrySend(messages, tryTime,timeout);
                } else {
                    throw e;
                }
            } catch (Exception e1) {
                throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION, e1);
            }
        } catch (MQClientException | RemotingException | InterruptedException e2) {
            throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION, e2);
        }
    }



    /**
     * εζ‘ζΆζ―ιθ―
     * @param message
     * @param retryTimes
     * @return
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     */
    private SendResult retrySend(Message message, int retryTimes) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        if (0 == retryTimes) {
            throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION);
        } else {
            Thread.sleep((long)((new Random()).nextInt(10) + 1));
            try {
                return this.defaultMQProducer.send(message);
            } catch (MQBrokerException e) {
                --retryTimes;
                return this.retrySend(message, retryTimes);
            }
        }
    }
    private SendResult retrySend(Message message, int retryTimes,Long timeout) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        if (0 == retryTimes) {
            throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION);
        } else {
            Thread.sleep((long)((new Random()).nextInt(10) + 1));
            try {
                return this.defaultMQProducer.send(message,timeout);
            } catch (MQBrokerException e) {
                --retryTimes;
                return this.retrySend(message, retryTimes,timeout);
            }
        }
    }


    /**
     * ε€ζ‘ζΆζ―ιθ―
     * @param messages
     * @param retryTimes
     * @return
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     */
    private SendResult retrySend(List<Message> messages, int retryTimes) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        if (0 == retryTimes) {
            throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION);
        } else {
            Thread.sleep((long)((new Random()).nextInt(10) + 1));
            try {
                return this.defaultMQProducer.send(messages);
            } catch (MQBrokerException e) {
                -- retryTimes;
                return this.retrySend(messages, retryTimes);
            }
        }
    }
    private SendResult retrySend(List<Message> messages, int retryTimes,Long timeout) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        if (0 == retryTimes) {
            throw new MyException(MqErrorCodeEnum.MQ_SYNC_SEND_FAIL_EXCEPTION);
        } else {
            Thread.sleep((long)((new Random()).nextInt(10) + 1));
            try {
                return this.defaultMQProducer.send(messages,timeout);
            } catch (MQBrokerException e) {
                -- retryTimes;
                return this.retrySend(messages, retryTimes,timeout);
            }
        }
    }


    /**
     * εΌζ­₯ζΆζ―
     * @param topic
     * @param tag
     * @param key
     * @param body
     * @param sendCallback
     */
    public void asnySend(String topic, String tag, String key, Object body, SendCallback sendCallback){
        try {
            this.defaultMQProducer.send(this.convert(topic, tag, key, body),sendCallback);
        } catch (RemotingException | InterruptedException | MQClientException e) {
            throw new MyException(MqErrorCodeEnum.MQ_ASNY_SEND_FAIL_EXCEPTION, e);
        }
    }

    /**
     * εΌζ­₯ε»ΆζΆζΆζ―
     * @param topic
     * @param tag
     * @param key
     * @param body
     * @param timeout
     * @param sendCallback
     */
    public void asnySend(String topic, String tag, String key, Object body, long timeout , SendCallback sendCallback){
        try {
            this.defaultMQProducer.send(this.convert(topic, tag, key, body),sendCallback, timeout);
        } catch (RemotingException | InterruptedException | MQClientException e) {
            throw new MyException(MqErrorCodeEnum.MQ_ASNY_SEND_FAIL_EXCEPTION, e);
        }
    }

    /**
     * εεζΆζ―
     * @param topic
     * @param tag
     * @param key
     * @param body
     */
    public void oneWaySend(String topic, String tag, String key, Object body){
        try {
            this.defaultMQProducer.sendOneway(this.convert(topic, tag, key, body));
        } catch (RemotingException | InterruptedException | MQClientException e) {
            throw new MyException(MqErrorCodeEnum.MQ_ONE_WAY_SEND_FAIL_EXCEPTION, e);
        }
    }
}
