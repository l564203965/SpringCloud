package com.example.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author: ljy  Date: 2020/2/26.
 */
@Configuration // 加上该注解 ，则不需要FeignClient里面加属性configuration
public class FeignHeadersInterceptor implements RequestInterceptor {

    private String tokenHeader = "authorization";

    @Override
    public void apply(RequestTemplate template) {

        HttpServletRequest request = getHttpServletRequest();

        if (Objects.isNull(request)) {
            return;
        }

        String authToken = request.getParameter(this.tokenHeader);
        if (StringUtils.isBlank(authToken)) {
            authToken = request.getHeader(this.tokenHeader);
        }
        template.header(this.tokenHeader, authToken);
        /*Map<String, String> headers = getHeaders(request);
        if (headers.size() > 0) {
            Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                // 把请求过来的header请求头 原样设置到feign请求头中
                // 包括token
                template.header(entry.getKey(), entry.getValue());
            }
        }*/
    }

    private HttpServletRequest getHttpServletRequest() {

        try {
            // 这种方式获取的HttpServletRequest是线程安全的
            return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            /*RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            return requestAttributes == null ? null : ((ServletRequestAttributes)requestAttributes).getRequest();*/
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {

        Map<String, String> map = new LinkedHashMap<>();

        Enumeration<String> enums = request.getHeaderNames();
        while (enums.hasMoreElements()) {
            String key = enums.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

}
