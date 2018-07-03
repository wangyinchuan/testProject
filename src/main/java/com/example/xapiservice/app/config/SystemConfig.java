package com.example.xapiservice.app.config;

import com.example.xapiservice.app.interceptor.VerifySignatureInterceptor;
import com.example.xapiservice.app.log.LogFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 系统配置
 * Created by guxuyang on 05/07/2017.
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.example.xapiservice.app.mapper")
public class SystemConfig extends WebMvcConfigurerAdapter {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public VerifySignatureInterceptor verifySignatureInterceptor() {
        return new VerifySignatureInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(verifySignatureInterceptor()).excludePathPatterns("/dnotdelet/mom.html");
        super.addInterceptors(registry);
    }

    @Bean
    public LogFilter getLogFilter() {
        return new LogFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(getLogFilter());
        registration.addUrlPatterns("/api/xapiservice/*");
        registration.setName("LogFilter");
        registration.setOrder(1);
        return registration;
    }

}


