package com.demo.scs.poc.shared.metrics;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;

@Configuration
@EnableMetrics
@Profile({ "prod" })
public class MetricsConfigProdEnvironment extends AbstractMetricsConfig {

    @Override
    public void configureReporters() {
        configureReporters(graphitePrefix("prod"));
    }
	
	@PostConstruct()
	public void init() {
		configureReporters();
	}
}
