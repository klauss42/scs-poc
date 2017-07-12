package com.demo.scs.poc.commons.consul;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

@Component
public class ConsulServiceLocator {

    private static final String SERVICE_NOT_AVAILABLE = "";

    @Autowired
    private DiscoveryClient discoveryClient;

    // TODO let app bean override this default and remove methods with ServiceLoadBalancer param
    private final ServiceLoadBalancer loadBalancer = new RandomLoadBalancer();

    public Optional<String> discoverServiceUrl(String serviceId) {
        return discoverServiceUrl(serviceId, this.loadBalancer);
    }

    public Optional<String> discoverServiceUrl(String serviceId, ServiceLoadBalancer loadBalancer) {
        Objects.requireNonNull(serviceId);
        Objects.requireNonNull(loadBalancer);

        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        Optional<ServiceInstance> optional = loadBalancer.selectInstance(instances);

        if (!optional.isPresent()) {
            return Optional.empty();
        }
        ServiceInstance serviceInstance = optional.get();

        String uri = serviceInstance.getUri().toString();
        String contextPath = serviceInstance.getMetadata().get("contextPath");
        if (contextPath != null && contextPath.length() > 0) {
            uri = uri + contextPath;
        }
        return Optional.of(uri);
    }

    public Optional<String> discoverServiceUrl(String serviceId, Map<String, String> cache) {
        return discoverServiceUrl(serviceId, this.loadBalancer, cache);
    }

    public Optional<String> discoverServiceUrl(String serviceId, ServiceLoadBalancer loadBalancer, Map<String, String> cache) {
        if (serviceId == null || serviceId.length() == 0) {
            return null;
        }
        String serviceUrl = cache.get(serviceId);
        if (serviceUrl != null) {
            if (SERVICE_NOT_AVAILABLE.equals(serviceUrl)) {
                return Optional.empty();
            }
            return Optional.of(serviceUrl);
        }

        Optional<String> optional = discoverServiceUrl(serviceId, loadBalancer);
        if (optional.isPresent()) {
            cache.put(serviceId, optional.get());
            return optional;
        }

        cache.put(serviceId, SERVICE_NOT_AVAILABLE);
        return Optional.empty();
    }
}
