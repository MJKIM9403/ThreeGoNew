package com.io.threegonew.config.jwt;

import com.io.threegonew.util.JavaMailSenderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public JavaMailSenderImpl javaMailSenderImpl() {
        return new JavaMailSenderImpl();
    }
}
