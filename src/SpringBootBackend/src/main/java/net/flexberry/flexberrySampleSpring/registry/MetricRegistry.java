package net.flexberry.flexberrySampleSpring.registry;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.FunctionTimer;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.util.MeterPartition;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import io.micrometer.core.lang.Nullable;
import net.flexberry.flexberrySampleSpring.configuration.MetricConfig;
import net.flexberry.flexberrySampleSpring.service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Stream;

import static io.micrometer.core.instrument.util.StringEscapeUtils.escapeJson;
import static java.util.stream.Collectors.joining;
import static java.util.stream.StreamSupport.stream;

public class MetricRegistry extends StepMeterRegistry {
    @Autowired
    JdbcTemplate jdbcTemplate;
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new NamedThreadFactory("sample-metrics-publisher");
    private LoggingService loggingService = new LoggingService();
    private final MetricConfig config;

    public MetricRegistry(MetricConfig config, Clock clock) {
        this(config, clock, DEFAULT_THREAD_FACTORY);
    }

    public MetricRegistry(MetricConfig config, Clock clock, ThreadFactory threadFactory) {
        super(config, clock);
        this.config = config;
        start(threadFactory);
    }

    @Override
    protected void publish() {
        try {
            for (List<Meter> batch : MeterPartition.partition(this, config.batchSize())) {
                String body = batch.stream().flatMap(meter -> meter.match(
                        m -> writeMeter(m), // visitGauge
                        m -> writeMeter(m), // visitCounter
                        timer -> writeTimer(timer), // visitTimer
                        summary -> writeSummary(summary), // visitSummary
                        m -> writeMeter(m), // visitLongTaskTimer
                        m -> writeMeter(m), // visitTimeGauge
                        m -> writeMeter(m), // visitFunctionCounter
                        timer -> writeFunctionTimer(timer), // visitFunctionTimer
                        m -> writeMeter(m)) // visitMeter
                ).collect(joining(",", "{\"metrics\":[", "]}"));

                loggingService.LogTrace("sending metrics batch to server:" + System.lineSeparator() + body);
            }
        } catch (Throwable e) {
            loggingService.LogError("failed to send metrics to server", e);
        }
    }

    private Stream<String> writeFunctionTimer(FunctionTimer timer) {
        long wallTime = clock.wallTime();

        Meter.Id id = timer.getId();
        // we can't know anything about max and percentiles originating from a function timer
        return Stream.of(
                writeMetric(id, "count", wallTime, timer.count()),
                writeMetric(id, "avg", wallTime, timer.mean(getBaseTimeUnit())),
                writeMetric(id, "sum", wallTime, timer.totalTime(getBaseTimeUnit())));
    }

    private Stream<String> writeTimer(Timer timer) {
        final long wallTime = clock.wallTime();
        final Stream.Builder<String> metrics = Stream.builder();

        Meter.Id id = timer.getId();
        metrics.add(writeMetric(id, "sum", wallTime, timer.totalTime(getBaseTimeUnit())));
        metrics.add(writeMetric(id, "count", wallTime, timer.count()));
        metrics.add(writeMetric(id, "avg", wallTime, timer.mean(getBaseTimeUnit())));
        metrics.add(writeMetric(id, "max", wallTime, timer.max(getBaseTimeUnit())));

        return metrics.build();
    }

    private Stream<String> writeSummary(DistributionSummary summary) {
        final long wallTime = clock.wallTime();
        final Stream.Builder<String> metrics = Stream.builder();

        Meter.Id id = summary.getId();
        metrics.add(writeMetric(id, "sum", wallTime, summary.totalAmount()));
        metrics.add(writeMetric(id, "count", wallTime, summary.count()));
        metrics.add(writeMetric(id, "avg", wallTime, summary.mean()));
        metrics.add(writeMetric(id, "max", wallTime, summary.max()));

        return metrics.build();
    }

    private Stream<String> writeMeter(Meter m) {
        long wallTime = clock.wallTime();
        return stream(m.measure().spliterator(), false)
                .map(ms -> {
                    Meter.Id id = m.getId().withTag(ms.getStatistic());
                    return writeMetric(id, null, wallTime, ms.getValue());
                });
    }

    @Override
    public void start(ThreadFactory threadFactory) {
        super.start(threadFactory);
    }

    @Override
    protected TimeUnit getBaseTimeUnit() {
        return TimeUnit.SECONDS;
    }

    private String writeMetric(Meter.Id meterId, @Nullable String suffix, long wallTime, double value) {
        Meter.Id fullId = meterId;
        String name =  meterId.getName();
        String dimension = config.getDimension();

        if (suffix != null) {
            fullId = fullId.withName(name + "." + suffix);
        }

        Iterable<Tag> tags = getConventionTags(fullId);

        //metadata
        String metadata = tags.iterator().hasNext()
                ? stream(tags.spliterator(), false)
                .map(t -> "\"" + escapeJson(t.getKey()) + ":" + escapeJson(t.getValue()) + "\"")
                .collect(joining(",", "{ \"tags\":[", "]}"))
                : "";

        String metricBuilder = "{" +
                "\"name\" :\"" + name + "\"" +
                ",\"value\": " + value +
                ",\"timestamp\": " + wallTime +
                ",\"dimension\": " + dimension +
                ",\"metadata\":" + metadata +
                "}";

        // Запись метрики в БД.
        Integer metric_id = GetMetric(name, dimension);

        SaveMetricValue(metric_id, value, wallTime, metadata);

        return metricBuilder;
    }

    private Integer GetMetric(String name, String dimensions) {
        Integer id = -1;

        try {
            id = jdbcTemplate.queryForObject("SELECT id FROM metric.metrics WHERE name = '" + name + "'", Integer.class);
        } catch (EmptyResultDataAccessException e) {
            loggingService.LogTrace("Metric '" + name + "' not found");
        }

        if (id < 0) {
            jdbcTemplate.update("INSERT INTO metric.metrics (\"name\", \"dimensions\") " +
                "VALUES (?, ? ::jsonb)", name, dimensions);

            id = jdbcTemplate.queryForObject("SELECT id FROM metric.metrics WHERE name = '" + name + "'", Integer.class);
        }

        return id;
    }

    private void SaveMetricValue(Integer metricId, double value, long wallTime, String metadata) {
        jdbcTemplate.update("INSERT INTO metric.\"values\" (\"metric_id\", \"value\", \"timestamp\", \"metadata\") " +
            "VALUES (?, ?, to_timestamp(" + wallTime + "), ? ::json)", metricId, value, metadata);
    }
}
