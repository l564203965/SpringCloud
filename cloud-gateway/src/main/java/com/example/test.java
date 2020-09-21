package com.example;

import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.util.StringUtils;

/**
 * @author: ljy  Date: 2020/9/17.
 */
public class test {

    public static void main(String[] args) {
        String text = "[{\"id\":\"consumer_route\",\"order\":0,\"predicates\":[{\"args\":{\"pattern\":\"/userConsumer/**,/config/**\"},\"name\":\"Path\"}],\"uri\":\"http://127.0.0.1:9998\"},{\"id\":\"provider-router\",\"order\":2,\"predicates\":[{\"args\":{\"pattern\":\"/provide1/**\"},\"name\":\"Path\"}],\"uri\":\"lb://nacos-provider\"}]";
        int eqIdx = text.indexOf(61);
        String[] args1 = StringUtils.tokenizeToStringArray(text.substring(eqIdx + 1), ",");
        for(int i = 1; i < args1.length; ++i) {
            System.out.println(args1[i]);
        }
    }

}
