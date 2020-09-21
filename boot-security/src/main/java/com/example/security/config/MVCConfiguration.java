package com.example.security.config;


import com.example.security.filter.CharacterEncodingIPFilter;
import com.example.security.utils.WebUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 2018/1/30
 * <p>
 * Spring MVC 扩展配置
 * <p>
 *
 * @author Shengzhao Li
 */
@Configuration
public class MVCConfiguration implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/");
    }

    /**
     * 扩展拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
//        registry.addInterceptor(vueViewInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/pay/alipayCallback", "/swagger-resources/**", "*.js", "/**/*.js", "*.css", "/**/*.css", "*.html", "/**/*.html");
    }


    /**
     * 解决乱码问题
     * For UTF-8
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.configureMessageConverters(converters);
        converters.add(new StringHttpMessageConverter(Charset.forName(WebUtils.UTF_8)));
    }


    /**
     * 字符编码配置 UTF-8
     */
    @Bean
    public FilterRegistrationBean encodingFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CharacterEncodingIPFilter());
        registrationBean.addUrlPatterns("/*");
        //值越小越靠前
        registrationBean.setOrder(1);
        return registrationBean;
    }


}
