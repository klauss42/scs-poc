package com.demo.scs.poc.profile.ui;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProfileUiApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ProfileUiApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
