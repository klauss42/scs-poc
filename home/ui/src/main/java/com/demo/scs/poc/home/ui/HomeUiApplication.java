package com.demo.scs.poc.home.ui;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.demo.scs.poc.commons.consul.ConsulServiceLocator;
import com.demo.scs.poc.commons.pssignal.PsSignalUrlService;
import com.demo.scs.poc.nav.fallback.NavFallback;

@SpringBootApplication
@Import({ NavFallback.class, ConsulServiceLocator.class, PsSignalUrlService.class })
@EnableDiscoveryClient
@EnableResourceServer
@EnableCircuitBreaker
public class HomeUiApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(HomeUiApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

}
