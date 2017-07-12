package com.demo.scs.poc.usercontext;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
public class UserContextApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserContextApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
