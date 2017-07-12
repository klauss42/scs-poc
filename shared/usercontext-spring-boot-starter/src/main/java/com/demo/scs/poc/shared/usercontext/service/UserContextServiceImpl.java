package com.demo.scs.poc.shared.usercontext.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.demo.scs.poc.commons.consul.ConsulServiceLocator;

public class UserContextServiceImpl implements UserContextService {

    private final Logger LOG = LoggerFactory.getLogger(UserContextServiceImpl.class);
    private final String SERVICE_ID = "user-context";

    private RestTemplate restTemplate;

    private ConsulServiceLocator consulServiceLocator;

    public UserContextServiceImpl(final RestTemplate restTemplate, final ConsulServiceLocator consulServiceLocator) {
        this.restTemplate = restTemplate;
        this.consulServiceLocator = consulServiceLocator;
    }

    @Override
    public Map<String, String> getContext(String userId) {
        String url = serviceUrl();
        if (url != null) {
            ResponseEntity<Data> response = null;
            try {
                response = restTemplate.getForEntity(url + "/" + userId, Data.class);
                return response.getBody() != null ? response.getBody().getData() : new HashMap<>();
            } catch (final HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    // ignore, this is a standard behavior in REST APIs
                } else {
                    LOG.error("Error while calling UserContext Service", e);
                }
            }
        }
        LOG.warn("No {} service discovered.", SERVICE_ID);
        return new HashMap<>();
    }

    @Override
    public void putContext(String userId, Map<String, String> map) {
        String url = serviceUrl();
        if (url != null) {
            try {
                Data data = new Data().setUserId(userId).setData(map);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Data> httpEntity = new HttpEntity<>(data, httpHeaders);
                restTemplate.postForObject(url + "/" + userId, httpEntity, Data.class);
            } catch (HttpClientErrorException e) {
                LOG.error("Error while calling UserContext Service", e);
            }
        }
        LOG.warn("No {} service discovered.", SERVICE_ID);
    }

    private String serviceUrl() {
        Optional<String> optionalServiceUrl = consulServiceLocator.discoverServiceUrl(SERVICE_ID);
        return optionalServiceUrl.map(s -> s + "/user").orElse(null);
    }

}
