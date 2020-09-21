package com.example;

import com.example.core.constant.AppConstant;
import com.example.feign.IUserClientFallback;
import feign.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ljy  Date: 2020/2/25.
 */
@SpringBootApplication// (exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement// 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@EnableDiscoveryClient
@EnableFeignClients(AppConstant.BASE_PACKAGES)
public class NacosConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class, args);
    }

    /*@LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    public class TestController {

        private final RestTemplate restTemplate;

        @Autowired
        public TestController(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

        @RequestMapping(value = "/echoCli/{str}", method = RequestMethod.GET)
        public String echoCli(@PathVariable String str) {
            return restTemplate.getForObject("http://boot-user/echoSer/" + str +"?authorization=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZWN1cml0eV91c2VyIiwiY3JlYXRlZCI6MTU4MjY4MjE5NzI0MX0.R2fmg26Ug5tk6KErytR6MdjTQhWEcA4vK9QYLClK2_3p5NfYNYw2DtiLGicCrveo-gerELIq6sd4qaGnzQQErw", String.class);
        }
    }*/

}
