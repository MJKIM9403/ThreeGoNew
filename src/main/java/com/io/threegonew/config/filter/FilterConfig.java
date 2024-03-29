package com.io.threegonew.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<ParamFilter> paramFilter() {
        FilterRegistrationBean<ParamFilter> registrationBean = new FilterRegistrationBean<>(new ParamFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/book")); // 필터 적용 url
        registrationBean.setOrder(1); // 필터 적용 순서

        return registrationBean;
    }
}
