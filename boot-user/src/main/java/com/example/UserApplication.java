package com.example;

import com.example.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableTransactionManagement// 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@EnableDiscoveryClient
@EnableFeignClients(AppConstant.BASE_PACKAGES)
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    /*@RestController
    class EchoController {
        @RequestMapping(value = "/echoSer/{str}", method = RequestMethod.GET)
        public String echoSer(@PathVariable String str) {
            return "Hello Nacos Discovery " + str;
        }
    }*/
}
