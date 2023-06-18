package net.flexberry.flexberrySampleSpring.configuration;

import io.micrometer.core.instrument.step.StepRegistryConfig;

import java.time.Duration;

public class MetricConfig implements StepRegistryConfig {
    private final Duration step = Duration.ofSeconds(10);

    private String dimension;

    public MetricConfig() {
        this.dimension = "{}";
    }

    @Override
    public String prefix() {
        return "SampleMetric";
    }

    @Override
    public String get(String s) {
        return null;
    }

    @Override
    public Duration step() {
        return step;
    }

    public String getDimension() {
        return dimension;
    }
}
