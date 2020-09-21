package com.example.security.filter;

import com.example.core.enums.State;
import com.example.security.entity.SOSUserDetails;
import com.example.security.utils.JwtTokenUtil;
import com.example.user.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private IUserService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //    @Value("${jwt.header}")
    private String tokenHeader = "authorization";

    private String reshe = "reshe";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    	// 放过这两个请求的方法
        if(request.getParameter(this.reshe) != null && request.getParameter(this.reshe).equals("pass") &&
        	(request.getRequestURI().contains("/queryAllAreaAndBusiness") || request.getRequestURI().contains("/queryReceivable") )){
            chain.doFilter(request, response);
        }
        if (!CorsUtils.isPreFlightRequest(request) && !request.getRequestURI().contains("/login")
                && !request.getRequestURI().contains("/getCaptcha")) {
            String authToken = request.getParameter(this.tokenHeader);
            if (StringUtils.isBlank(authToken)) {
                authToken = request.getHeader(this.tokenHeader);
            }

            // 从Token中取得员工编码
            String name = jwtTokenUtil.getUsernameFromToken(authToken);
            response.setHeader("Access-Control-Allow-Origin","*");
            response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
            response.setHeader("Access-Control-Max-Age","3600");
            response.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
            response.setHeader("","");response.setHeader("","");

            if (name == null || name.equals("")) {
                logger.error("登录时间过期！");
                response.sendError(State.bad_token.getCode(), "登录时间过期！");
                return;
            }

            UserDetails jwtUser = userDetailsService.loadUserByUsername(name);
            // 只要token验证通过，就将用户信息令牌放入security环境
            if (jwtTokenUtil.validateToken(authToken, jwtUser)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        jwtUser, null, jwtUser.getAuthorities());
                /*// 获取表单输入中返回的用户名
                String userName = (String) authentication.getPrincipal();
                // 获取表单中输入的密码
                String password = (String) authentication.getCredentials();*/
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.error("登录验证失败！");
                response.sendError(State.bad_token.getCode(), "登录验证失败！");
                return;
            }

            /** 检查此用户菜单权限 */
//            boolean hasAccess = systemService.checkAccess(request.getRequestURI(), jwtUser.getEmpCode());
            boolean hasAccess = true;

            if (!hasAccess) {
                logger.error(jwtUser.getUsername() + "没有菜单权限！");
                response.sendError(State.unauthorized.getCode(), "没有菜单权限！");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}