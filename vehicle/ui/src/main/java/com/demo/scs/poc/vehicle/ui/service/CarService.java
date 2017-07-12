package com.demo.scs.poc.vehicle.ui.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import com.demo.scs.poc.commons.consul.ConsulServiceLocator;
import com.demo.scs.poc.vehicle.ui.beans.Car;

@Service
@EnableScheduling
public class CarService {

    private final Logger LOG = LoggerFactory.getLogger(CarService.class);

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Autowired
    private ConsulServiceLocator consulServiceLocator;


    public List<Car> findAll() {
        String url = serviceUrl();
        if (url != null) {
            LOG.info("Calling URL {}", url);
            ResponseEntity<Resources<Resource<Car>>> response = restTemplate
                .exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<Resources<Resource<Car>>>() {});

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody().getContent().stream()
                    .map(Resource::getContent).collect(Collectors.toList());
            }
            return Collections.emptyList();
        }
        LOG.warn("No vehicle-service1 service discovered. ");
        return Collections.emptyList();
    }

    public Car findById(Long id) {
        String url = serviceUrl();
        if (url != null) {
            return restTemplate.getForObject(url + "/" + id, Car.class);
        }
        LOG.warn("No vehicle-service1 service discovered. ");
        return null;
    }

    /**
     * This method triggers a scheduled restTemplate call just to verify that server-to-server communication
     * via OAuth2 is also working. In this request there is no user logged in and authentication should work via
     * client-credentials.
     */
    @Scheduled(fixedDelay = 30*1000)
    void cronDemo() {
        LOG.info("scheduled server-to-server call: response: {}", findAll());
    }

    private String serviceUrl() {
        Optional<String> optionalServiceUrl = consulServiceLocator.discoverServiceUrl("vehicle-service1");
        return optionalServiceUrl.map(s -> s + "/car").orElse(null);
    }
}
