package net.flexberry.flexberrySampleSpring.configuration;

import io.micrometer.core.instrument.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.flexberry.flexberrySampleSpring.registry.MetricRegistry;

@Configuration
public class MetricClientConfig {
    @Bean
    public MetricRegistry getMetricRegistry() {
        return new MetricRegistry(new MetricConfig(), Clock.SYSTEM);
    }
}