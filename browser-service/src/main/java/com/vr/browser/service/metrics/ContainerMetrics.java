package com.vr.browser.service.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ContainerMetrics {

    private final MeterRegistry registry;

    public ContainerMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    @PostConstruct
    public void init() {
        // Register memory usage gauge
        registry.gauge("container.memory.usage.bytes", this, ContainerMetrics::getMemoryUsage);

        // Register CPU usage gauge
        registry.gauge("container.cpu.usage.percent", this, ContainerMetrics::getCpuUsagePercent);
    }

    public double getMemoryUsage() {
        try {
            long used = Long.parseLong(Files.readString(Paths.get("/sys/fs/cgroup/memory/memory.usage_in_bytes")).trim());
            long limit = Long.parseLong(Files.readString(Paths.get("/sys/fs/cgroup/memory/memory.limit_in_bytes")).trim());
            return (double) used / limit * 100; // memory usage in %
        } catch (Exception e) {
            return 0;
        }
    }

    private long prevCpu = 0;
    private long prevTime = System.nanoTime();

    public double getCpuUsagePercent() {
        try {
            long current = Long.parseLong(Files.readString(Paths.get("/sys/fs/cgroup/cpu/cpuacct.usage")).trim());
            long now = System.nanoTime();
            long deltaCpu = current - prevCpu;
            long deltaTime = now - prevTime;

            prevCpu = current;
            prevTime = now;

            int cores = Runtime.getRuntime().availableProcessors();
            return (double) deltaCpu / deltaTime * 100 / cores;
        } catch (Exception e) {
            return 0;
        }
    }
}
