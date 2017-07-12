package com.demo.scs.poc.shared.usercontext.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import com.demo.scs.poc.commons.consul.ConsulServiceLocator;
import com.demo.scs.poc.shared.usercontext.service.UserContextService;
import com.demo.scs.poc.shared.usercontext.service.UserContextServiceImpl;

@Configuration
public class UserContextConfig {

    private static final Logger LOG = LoggerFactory.getLogger(UserContextConfig.class);

    @Autowired
    ConsulServiceLocator consulServiceLocator;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Bean
    UserContextService userContextService() {
        return new UserContextServiceImpl(restTemplate, consulServiceLocator);
    }
}
