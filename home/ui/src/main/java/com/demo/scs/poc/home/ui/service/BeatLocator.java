package com.demo.scs.poc.home.ui.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class BeatLocator {

    private static final Logger LOG = LoggerFactory.getLogger(BeatLocator.class);

    // XXX mapping should be part of api gateway / zuul routing ...
    private static final Map<String, String> BEAT_SERVICE_MAPPINGS;

    static {
        HashMap<String, String> map = new HashMap<>(4);
        map.put("connect", "connect-ui");
        map.put("vehicle", "vehicle-ui");

        BEAT_SERVICE_MAPPINGS = map;
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @HystrixCommand(fallbackMethod = "availableBeatProvidersFallback")
    public Map<String, String> availableBeatProviders() {
        List<String> services = discoveryClient.getServices();
        if (services == null || services.size() == 0) {
            return Collections.emptyMap();
        }
        HashSet<String> servicesSet = new HashSet<>(services);
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : BEAT_SERVICE_MAPPINGS.entrySet()) {
            String serviceId = entry.getValue();
            // XXX filter also on beat-provider tag?
            if (servicesSet.contains(serviceId)) {
                result.put(entry.getKey(), serviceId);
            }
        }
        return result;
    }

    @SuppressWarnings("unused")
    public Map<String, String> availableBeatProvidersFallback(Throwable e) {
        LOG.warn("Could not get available beat providers.", e);
        return Collections.emptyMap();
    }
}
