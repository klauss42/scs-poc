package com.demo.scs.poc.home.service1;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
public class HomeService1Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(HomeService1Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
