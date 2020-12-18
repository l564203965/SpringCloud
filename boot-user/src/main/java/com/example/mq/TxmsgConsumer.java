package com.example.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author: ljy  Date: 2020/10/8.
 */
@Component
@RocketMQMessageListener(topic = "topic_txmsg",consumerGroup = "consumer_txmsg_group_user")
@Slf4j
public class TxmsgConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.info("开始消费消息:{}",s);
        //解析消息为对象
        /*final JSONObject jsonObject = JSON.parseObject(s);
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(jsonObject.getString("accountChange"),AccountChangeEvent.class);

        //调用service增加账号金额
        accountChangeEvent.setAccountNo("2");
        accountInfoService.addAccountInfoBalance(accountChangeEvent);*/
        log.info("消费消息完成:{}",s);
    }

}
