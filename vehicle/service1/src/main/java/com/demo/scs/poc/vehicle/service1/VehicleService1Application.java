package com.demo.scs.poc.vehicle.service1;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableResourceServer
public class VehicleService1Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(VehicleService1Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
