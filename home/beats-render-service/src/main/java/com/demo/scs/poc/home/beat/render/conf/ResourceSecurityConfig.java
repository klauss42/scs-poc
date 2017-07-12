package com.demo.scs.poc.home.beat.render.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ResourceSecurityConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
//                .antMatchers("/template**").permitAll()
                .anyRequest().authenticated()
                .and()
            .csrf()
                .disable();
        // @formatter:on
    }
}
