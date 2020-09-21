package com.example.feign;

import com.example.core.wrapper.WrapMapper;
import com.example.core.wrapper.Wrapper;
import com.example.user.entity.vo.UserVo;
import com.example.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: ljy  Date: 2020/2/27.
 */
@RestController
public class UserClient implements IUserClient{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IUserService userService;

    @Override
    @ApiOperation(httpMethod = "GET", value = "修改用户信息")
    public Wrapper getUserByName(@RequestParam String name) {
        UserVo orderDoc = userService.getUserByName(name);
        return WrapMapper.ok(orderDoc);
    }

    /**
     * 查询单条
     */
    @ApiOperation(httpMethod = "GET", value = "查询单个用户")
    public Wrapper getUserById(@RequestParam Long id) {
        logger.info("createOrderDoc - 查询单个用户. id={}", id);
        UserVo orderDoc = userService.getUserById(id);
        return WrapMapper.ok(orderDoc);
    }
}
