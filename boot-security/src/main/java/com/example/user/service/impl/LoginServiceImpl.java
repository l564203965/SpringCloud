package com.example.user.service.impl;

import com.example.security.entity.SOSUserDetails;
import com.example.security.utils.JwtTokenUtil;
import com.example.user.entity.dto.RequestResultDto;
import com.example.user.service.ILoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: ljy  Date: 2020/2/19.
 */
@Service
public class LoginServiceImpl implements ILoginService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public RequestResultDto login(String name, String password) {
        logger.info("验证账号密码");
        RequestResultDto resultDto = new RequestResultDto();
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        name,
                        password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final SOSUserDetails jwtUser = (SOSUserDetails) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateToken(jwtUser);
        resultDto.setToken(token);
        return resultDto;
    }
}
