package com.example.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.core.entity.User;
import com.example.core.wrapper.WrapMapper;
import com.example.core.wrapper.Wrapper;
import com.example.feign.IUserClient;
import com.example.user.entity.vo.UserVo;
import com.example.user.service.IUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author: ljy  Date: 2020/2/27.
 */
@RestController
@RequestMapping("/userConsumer")
public class UserConsumerController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IUserClient userClient;

    @Resource
    private IUserService userService;

    /**
     * 修改用户
     * bindingResult 验证入参
     */
    @PostMapping("/updateUserById")
    @ApiOperation(httpMethod = "POST", value = "修改用户")
    @SentinelResource(value="updateUserById")
    public Wrapper updateUserById(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                return WrapMapper.error(error.getDefaultMessage());
            }
        }
        logger.info("addUser - 修改用户. user={}", user);
        try {
            userService.updateUserById(user);
            return WrapMapper.ok();
        } catch (Exception e) {
            logger.error("修改用户失败",e);
            return WrapMapper.error();
        }
    }

    /**
     * 查询用户
     * Swagger 配置用于生成在线接口文档
     * 测试降级规则
     */
    @GetMapping("/getUserByName")
    @ApiOperation(httpMethod = "POST", value = "查询单个用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "主键", dataType = "long", paramType = "query")})
    @SentinelResource(value="getUserByName",blockHandler = "fallback_hostkey")
    public Wrapper getUserByName(@RequestParam(value = "name", required = true) String name) {
        logger.info("getUserByName - 查询用户. name={}", name);
        try {
            if ("philips".equals(name)) {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return userClient.getUserByName(name);
        }catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error("查询错误");
        }
    }

    // 修改sentinel 降级的默认回执方法(传递参数的一致性，并且需要携带 BlockException),
    // 若希望使用其他类的函数，则可以指定 blockHandlerClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析
    public Wrapper fallback_hostkey(String name, BlockException blockException){
        return WrapMapper.error("限流咯。。。。");
    }

    /**
     * 测试流控规则
     * @param id
     * @return
     */
    @GetMapping("/getUserById/{id}")
    @ApiOperation(httpMethod = "POST", value = "查询单个用户")
//    @SentinelResource(value="getUserById")
    public Wrapper getUserById(@PathVariable(value="id") Long id) {
        logger.info("getUserById - 查询单个用户. id={}", id);

        // 1.5.0 版本开始可以利用 try-with-resources 特性
        // 这里不使用try-with-resources是因为Tracer.trace会统计不上异常
        Entry entry = null;
        // 资源名可使用任意有业务语义的字符串，比如方法名、接口名或其它可唯一标识的字符串。
        // 定义一个sentinel保护的资源，名称为test-sentinel-api
        try {
            entry = SphU.entry("getUserById", EntryType.IN);
            // 被保护的业务逻辑
            System.out.println("查询单个用户调用一次");
            // 标识对test-sentinel-api调用来源为test-origin（用于流控规则中“针对来源”的配置）
            ContextUtil.enter("getUserById", "test-origin");
        } catch (BlockException ex) {
            // 资源访问阻止，被限流或被降级
            // 在此处进行相应的处理操作
            ex.printStackTrace();
            return WrapMapper.error("操作太频繁");
        } catch (Exception e) {
            // 对业务异常进行统计
            Tracer.trace(e);
            e.printStackTrace();
            return WrapMapper.error("查询错误");
        } finally {
            if (entry != null) {
                entry.exit();
            }
             ContextUtil.exit();
        }

        try {
            return userClient.getUserById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error("查询错误");
        }
    }

}
