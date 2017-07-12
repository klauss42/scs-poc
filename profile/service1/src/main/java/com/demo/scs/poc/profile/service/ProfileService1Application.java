package com.demo.scs.poc.profile.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProfileService1Application {

    public static void main(String[] args) {
        SpringApplication.run(ProfileService1Application.class, args);
    }
}

