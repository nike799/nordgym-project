package com.nordgym.configuration;

import com.nordgym.interceptors.UserAccessInterceptor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@Configuration
public class ApplicationBeanConfiguration implements WebMvcConfigurer {
    private final UserAccessInterceptor userAccessInterceptor;

    public ApplicationBeanConfiguration(UserAccessInterceptor userAccessInterceptor) {
        this.userAccessInterceptor = userAccessInterceptor;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAccessInterceptor).addPathPatterns("/","/gallery","/contacts","/services","/training-programs/*");
    }
}
