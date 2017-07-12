package com.demo.scs.poc.home.ui.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.demo.scs.poc.beats.api.v1.Beat;

@Component
public class BeatRenderService {

    private static final Logger LOG = LoggerFactory.getLogger(BeatRenderService.class);

    @Value("${spring.application.name}")
    private String serviceId;

    //    @Autowired
    //    @Qualifier("clientCredentialsRestTemplate")
    //    private RestTemplate restTemplate;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "renderBeatFallback")
    public Optional<String> renderBeat(String renderServiceUrl, Beat beat, String pageId) {
        if (beat.getData() == null) {
            LOG.error("Null data (of beat " + beat.getId() + "/" + beat.getTemplate() + ") not implemented yet");
            return Optional.empty();
        }
        String url = renderServiceUrl + '/' + beat.getTemplate() + "?beatId=" + beat.getId() + "&serviceId=" + serviceId
                + "&pageId=" + pageId;
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(beat.getData(), httpHeaders);
            String html = restTemplate.postForObject(new URI(url), httpEntity, String.class);
            return Optional.of(html);
        } catch (RestClientException | URISyntaxException e) {
            LOG.error("Could not render beat using url " + url, e);
            return Optional.empty();
        }
    }

    @SuppressWarnings("unused")
    Optional<String> renderBeatFallback(String renderServiceUrl, Beat beat, String pageId, Throwable e) {
        LOG.warn("Could not render beat " + beat.getId() + " using url " + renderServiceUrl, e);
        return Optional.empty();
    }
}
