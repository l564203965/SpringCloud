package com.example.mq;

import com.alibaba.fastjson.JSONObject;
import com.example.core.wrapper.WrapMapper;
import com.example.core.wrapper.Wrapper;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: ljy  Date: 2020/10/8.
 */
@RestController
public class MQProducer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * mq发送事务消息
     * @param msg
     * @return
     */
    @GetMapping("/sendMQMessage")
    public Wrapper sendMQMessage(@RequestParam(value = "msg", required = true) String msg) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("arg", "test");
        Message<String> message = MessageBuilder.withPayload(msg).build();
        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction("producer_group_txmsg_nacos", "topic_txmsg", message, jsonObject);
        logger.info("send transcation message body={},result={}",message.getPayload(),sendResult.getSendStatus());
        return WrapMapper.ok();
    }

//    /**
//     * mq发送普通消息
//     * @param message
//     * @throws MQClientException
//     * @throws RemotingException
//     * @throws InterruptedException
//     * @throws MQBrokerException
//     */
//    @GetMapping("/send")
//    public void send(String message) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
//        DefaultMQProducer producer = new DefaultMQProducer("test_producer_group");
//        producer.setNamesrvAddr("127.0.0.1:9876");//("192.168.0.1:9876;192.168.0.2:9876");
//        producer.start();
//
//        org.apache.rocketmq.common.message.Message msg = new org.apache.rocketmq.common.message.Message("test-topic", "test-tag", message.getBytes());
//        producer.send(msg);
//    }

}
