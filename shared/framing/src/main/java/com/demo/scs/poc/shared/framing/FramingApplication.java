package com.demo.scs.poc.shared.framing;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
public class FramingApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FramingApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
