package com.example.feign;

import com.example.core.constant.AppConstant;
import com.example.core.entity.User;
import com.example.core.wrapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: ljy  Date: 2020/2/27.
 */
@FeignClient(
        value = AppConstant.APPLICATION_USER_NAME,
        fallback = IUserClientFallback.class
)
public interface IUserClient {

    String API_PREFIX = "/user";

    /**
     * 查询用户
     * @param name
     * @return
     */
    @GetMapping(API_PREFIX + "/getUserByName")
    Wrapper getUserByName(@RequestParam("name") String name);

    @GetMapping(API_PREFIX + "/getUserById")
    Wrapper getUserById(@RequestParam("id") Long id);
}
