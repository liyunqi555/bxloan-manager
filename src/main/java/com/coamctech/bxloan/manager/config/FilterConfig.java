package com.coamctech.bxloan.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2015/4/16.
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FilterConfig {

    @Value("/api1/app/*")
    private String appUrlPattern;
    @Value("/api/back/*")
    private String backUrlPattern;
    @Bean
    public FilterRegistrationBean appRequestFilter() {
        AppRequestFilter filter = createAppRequestFilter();
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        List<String> urlPatterns = Arrays.asList(appUrlPattern.split(";"));
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean backRequestFilter() {
        BackRequestFilter filter = createBackRequestFilter();
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        List<String> urlPatterns = Arrays.asList(backUrlPattern.split(";"));
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }



    @Bean
    public AppRequestFilter createAppRequestFilter() {
        return new AppRequestFilter();
    }
    @Bean
    public BackRequestFilter createBackRequestFilter() {
        return new BackRequestFilter();
    }
}