package com.demo.scs.poc.platform.gateway;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GatewayApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Configuration
    @EnableOAuth2Sso
    protected static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .logout().and()
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
            // @formatter:on
        }
    }

}
