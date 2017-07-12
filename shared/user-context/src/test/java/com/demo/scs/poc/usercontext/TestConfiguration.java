package com.demo.scs.poc.usercontext;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Configuration
@EnableAuthorizationServer
public class TestConfiguration extends AuthorizationServerConfigurerAdapter {

    public static final String CLIENT_ID = "testClient";

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient(CLIENT_ID)
            .authorizedGrantTypes("password");
    }
}
