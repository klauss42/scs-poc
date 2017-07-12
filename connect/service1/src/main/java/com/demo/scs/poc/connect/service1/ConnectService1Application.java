package com.demo.scs.poc.connect.service1;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
public class ConnectService1Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ConnectService1Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
