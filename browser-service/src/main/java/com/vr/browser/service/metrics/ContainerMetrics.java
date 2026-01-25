package com.vr.browser.service.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@Slf4j
@Profile("default")
public class ContainerMetrics {

    private static final Path CPU_STAT = Path.of("/sys/fs/cgroup/cpu.stat");
    private static final Path MEMORY_CURRENT = Path.of("/sys/fs/cgroup/memory.current");

    private volatile double cpuPercent = 0.0;
    private volatile long memoryBytes = 0;

    private long lastCpuUsageMicros = 0;
    private long lastSampleTimeNanos = 0;

    @Scheduled(fixedRate = 2000)
    public void updateMetrics() {
        try {
            updateCpu();
            updateMemory();
        } catch (Exception exception) {
            log.error("", exception);
        }
    }

    private void updateCpu() throws IOException {
        long currentCpuUsageMicros = readCpuUsageMicros();
        long now = System.nanoTime();

        if (lastSampleTimeNanos > 0) {
            long cpuDeltaMicros = currentCpuUsageMicros - lastCpuUsageMicros;
            long timeDeltaNanos = now - lastSampleTimeNanos;

            if (timeDeltaNanos > 0) {
                cpuPercent = (cpuDeltaMicros / 1_000_000.0) /
                        (timeDeltaNanos / 1_000_000_000.0)
                        * 100.0;
            }
        }

        lastCpuUsageMicros = currentCpuUsageMicros;
        lastSampleTimeNanos = now;
    }

    private void updateMemory() throws IOException {
        try {
            memoryBytes = Long.parseLong(Files.readString(MEMORY_CURRENT).trim());
        } catch (Exception exception) {
            log.error("", exception);
            throw exception;
        }
    }

    private long readCpuUsageMicros() throws IOException {
        try {
            List<String> lines = Files.readAllLines(CPU_STAT);
            for (String line : lines) {
                if (line.startsWith("usage_usec")) {
                    return Long.parseLong(line.split(" ")[1]);
                }
            }
            return 0;
        } catch (Exception exception) {
            log.error("", exception);
            throw exception;
        }
    }

    // ======================
    // Prometheus Gauges
    // ======================

    @Bean
    public Gauge containerCpuUsagePercent(MeterRegistry registry) {
        return Gauge.builder("container_cpu_usage_percent", this, m -> m.cpuPercent)
                .description("Total container CPU usage percentage (includes JVM + Chrome)")
                .register(registry);
    }

    @Bean
    public Gauge containerMemoryUsageBytes(MeterRegistry registry) {
        return Gauge.builder("container_memory_usage_bytes", this, m -> m.memoryBytes)
                .description("Total container memory usage in bytes (includes JVM + Chrome)")
                .register(registry);
    }
}
