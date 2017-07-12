package com.demo.scs.poc.commons.consul;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

@Component
public class RandomLoadBalancer implements ServiceLoadBalancer {

    @Override
    public Optional<ServiceInstance> selectInstance(List<ServiceInstance> instances) {
        if (instances == null || instances.size() == 0) {
            return Optional.empty();
        }
        final int idx;
        if (instances.size() == 1) {
            idx = 0;
        } else {
            idx = (int) (Math.random() * instances.size());
        }
        ServiceInstance instance = instances.get(idx);
        return Optional.of(instance);
    }
}
