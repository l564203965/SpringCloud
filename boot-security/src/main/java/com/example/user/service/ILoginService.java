package com.example.user.service;

import com.example.user.entity.dto.RequestResultDto;

/**
 * @author: ljy  Date: 2020/2/19.
 */
public interface ILoginService {

    /**
     * 登录
     * @param name
     * @param password
     * @return
     */
    RequestResultDto login(String name, String password);
}
