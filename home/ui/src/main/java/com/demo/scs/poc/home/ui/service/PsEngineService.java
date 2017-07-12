package com.demo.scs.poc.home.ui.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.demo.scs.poc.home.ui.beans.PsEngineBeat;

@Component
public class PsEngineService {

    private static final Logger LOG = LoggerFactory.getLogger(PsEngineService.class);

    @Value("${scspoc.ps-engine.url}")
    private String psEngineUrl;

//    @Autowired
//    @Qualifier("clientCredentialsRestTemplate")
//    private RestTemplate restTemplate;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @PostConstruct
    public void postConstruct() {
        LOG.debug("Constructed: psEngineUrl={}", psEngineUrl);
    }

    @HystrixCommand(fallbackMethod = "queryBeatsForFallback")
    public List<PsEngineBeat> queryBeatsFor(String userId) {
        StringBuilder sb = new StringBuilder(psEngineUrl).append("/pulse");
        if (userId != null && userId.length() > 0) {
            sb.append('/').append(userId);
            String url = sb.toString();
            PsEngineBeat[] arr = restTemplate.getForObject(url, PsEngineBeat[].class);
            if (arr == null) {
                LOG.warn("Got null from ps-engine recommender at " + psEngineUrl + " for user " + userId);
                return Collections.emptyList();
            }
            return Arrays.asList(arr);
        } else {
            LOG.warn("Could not query ps-engine beats, no user id available. Returning empty list.");
            return Collections.emptyList();
        }

    }

    @SuppressWarnings("unused")
    List<PsEngineBeat> queryBeatsForFallback(String userId, Throwable e) {
        LOG.warn("Could not query ps-engine beats for user " + userId + ". Returning empty list.", e);
        return Collections.emptyList();
    }
}
