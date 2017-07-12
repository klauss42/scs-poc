package com.demo.scs.poc.shared.metrics;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteReporter.Builder;
import com.codahale.metrics.graphite.GraphiteSender;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

/**
 * Based on https://craftsmen.nl/index.php/application-monitoring-with-graphite-an-example-how-to-integrate-dropwizard-metrics-in-a-spring-boot-application
 * and https://github.com/Manfred73/graphite-monitor-example
 */
public abstract class AbstractMetricsConfig extends MetricsConfigurerAdapter {
    
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMetricsConfig.class);
    
    @Value("${graphite.enabled:true}")
    private boolean enabled;

    @Value("${graphite.host:localhost}")
    private String graphiteHost;

    @Value("${graphite.port:2003}")
    private int graphitePort;

    @Value("${graphite.reportperiod:1000}")
    private int graphiteReportPeriod;

    @Value("${spring.application.name:unknown}")
    private String appName;

	@Autowired
	private MetricRegistry registry;

	private String graphitePrefix;

	abstract protected void configureReporters();

	protected void configureReporters(String graphitePrefix) {
		this.graphitePrefix = graphitePrefix;
		configureReporters(registry);
	}

	@Override
	public void configureReporters(MetricRegistry metricRegistry) {
        LOG.info("Initializing Graphite Reporter with Host {}:{} and prefix {}", graphiteHost, graphitePort, graphitePrefix);
		registerReporter(JmxReporter.forRegistry(metricRegistry).build()).start();
		GraphiteReporter graphiteReporter = getGraphiteReporterBuilder(metricRegistry).build(getGraphite());
		registerReporter(graphiteReporter);
		graphiteReporter.start(graphiteReportPeriod, TimeUnit.MILLISECONDS);
	}

	private Builder getGraphiteReporterBuilder(MetricRegistry metricRegistry) {
		metricRegistry.register("gc", new GarbageCollectorMetricSet());
		metricRegistry.register("memory", new MemoryUsageGaugeSet());
		metricRegistry.register("threads", new ThreadStatesGaugeSet());
		metricRegistry.register("os", new OperatingSystemGaugeSet());
        // @formatter:off
		return GraphiteReporter
            .forRegistry(metricRegistry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .filter(MetricFilter.ALL)
            .prefixedWith(graphitePrefix);
		// @formatter:on
	}

	private GraphiteSender getGraphite() {
        return new ExceptionIgnoringGraphite(new Graphite(new InetSocketAddress(graphiteHost, graphitePort)));
	}

    protected String graphitePrefix(String environment) {
        return "scs-poc." + environment + "." + appName + "." + hostname();
    }

    private String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName().replace('.', '_');
        } catch (UnknownHostException e) {
            return "dockerhost";
        }
    }

    public static class ExceptionIgnoringGraphite implements GraphiteSender {

        private final GraphiteSender graphiteSender;

        ExceptionIgnoringGraphite(GraphiteSender graphiteSender) {
            this.graphiteSender = graphiteSender;
        }

        @Override
        public void connect() throws IllegalStateException, IOException {
            doAndLogException(graphiteSender::connect);
        }

        @Override
        public void send(String name, String value, long timestamp) throws IOException {
            doAndLogException(() -> graphiteSender.send(name, value, timestamp));
        }

        @Override
        public void flush() throws IOException {
            doAndLogException(graphiteSender::flush);
        }

        @Override
        public boolean isConnected() {
            return graphiteSender.isConnected();
        }

        @Override
        public int getFailures() {
            return graphiteSender.getFailures();
        }

        @Override
        public void close() throws IOException {
            doAndLogException(graphiteSender::close);
        }

        void doAndLogException(Ignorable ignorable) {
            try {
                ignorable.doAndLogException();
            } catch (Exception e) {
                LOG.trace("Exception occurred while trying to connect to Graphite: {}", e.getMessage());
            }
        }
    }

    interface Ignorable {
        void doAndLogException() throws Exception;
    }

}
