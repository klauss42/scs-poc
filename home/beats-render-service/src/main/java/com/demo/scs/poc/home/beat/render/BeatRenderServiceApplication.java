package com.demo.scs.poc.home.beat.render;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.demo.scs.poc.commons.pssignal.PsSignalUrlService;

@SpringBootApplication
@Import({ PsSignalUrlService.class })
@EnableDiscoveryClient
@EnableResourceServer
public class BeatRenderServiceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BeatRenderServiceApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}

