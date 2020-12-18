package com.example.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: ljy  Date: 2020/10/8.
 */
@Component
@Slf4j
@RocketMQTransactionListener(txProducerGroup = "producer_group_txmsg_nacos")
public class ProducerTxmsgListener implements RocketMQLocalTransactionListener {

    // 消息发送成功回调此方法，此方法执行本地事务
    @Override
//    @Transactional
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        // 解析消息内容
        try {
            String jsonString = new String((byte[]) message.getPayload());
            log.info("消息发送成功，返回消息{}",jsonString);
            // 制行本地事务成功commit
            log.info("制行本地事务提交业务方法");
            // 调用service扣除账号金额
            // service.updateAccountAmount(args);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            log.error("executeLocalTransaction 事务执行失败",e);
            e.printStackTrace();
            // 制行本地事务失败rollback
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    // 此方法检查事务执行状态
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        RocketMQLocalTransactionState state;
        /*final JSONObject jsonObject = JSON.parseObject(new String((byte[]) message.getPayload()));
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(jsonObject.getString("accountChange"),AccountChangeEvent.class);

        // 事务id
        String txNo = accountChangeEvent.getTxNo();
        int isexistTx = accountInfoDao.isExistTx(txNo);
        log.info("回查事务，事务号: {} 结果: {}", accountChangeEvent.getTxNo(),isexistTx);
        // 执行成功
        if(isexistTx>0){
            state=  RocketMQLocalTransactionState.COMMIT;
        }else{
            state=  RocketMQLocalTransactionState.UNKNOWN;
        }*/

        log.info("回查事务，message: {} ", message);
        if (StringUtils.isNotBlank(new String((byte[]) message.getPayload()))) {
            state=  RocketMQLocalTransactionState.COMMIT;
        } else {
            state=  RocketMQLocalTransactionState.UNKNOWN;
        }
        return state;
    }


}
