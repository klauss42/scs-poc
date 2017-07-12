package com.demo.scs.poc.shared.auth;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
public class AuthApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AuthApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

}
