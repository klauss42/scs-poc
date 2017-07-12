package com.demo.scs.poc.commons.consul;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.client.ServiceInstance;

@FunctionalInterface
public interface ServiceLoadBalancer {

    Optional<ServiceInstance> selectInstance(List<ServiceInstance> instances);
}
