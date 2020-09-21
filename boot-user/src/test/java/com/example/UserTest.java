package com.example;

import com.example.user.entity.vo.UserVo;
import com.example.user.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: ljy  Date: 2020/2/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private IUserService userService;

    @Test
    public void testGetUserById() {
        UserVo vo = userService.getUserById(1L);
        System.out.println(vo);
    }

}
