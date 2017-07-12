package com.demo.scs.poc.connect.ui.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.demo.scs.poc.commons.consul.ConsulServiceLocator;
import com.demo.scs.poc.nav.fallback.NavFallback;

@Component
public class NavigationService {

    private static final Logger LOG = LoggerFactory.getLogger(NavigationService.class);

//    @Autowired
//    @Qualifier("clientCredentialsRestTemplate")
//    private RestTemplate restTemplate;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Autowired
    private ConsulServiceLocator consulServiceLocator;

    @Autowired
    private NavFallback navFallback;

    @Value("${spring.application.name}")
    private String serviceId;

    @HystrixCommand(fallbackMethod = "getNavbarFallback")
    public String getNavbar(HttpServletRequest req) {
        Optional<String> optionalServiceUrl = consulServiceLocator.discoverServiceUrl("shared-framing");
        if (optionalServiceUrl.isPresent()) {
            String url = optionalServiceUrl.get() + "/v1/navbar?current=" + serviceId;
            return restTemplate.getForObject(url, String.class);
        }

        LOG.warn("No shared-framing service discovered. Using fallback navi.");
        return getNavbarFallback(req, null);
    }

    String getNavbarFallback(HttpServletRequest req, Throwable e) {
        if (e != null) {
            LOG.warn("Could not get navbar from remote service. Using fallback.", e);
        }
        return navFallback.getNavFallbackHtml(req);
    }
}
