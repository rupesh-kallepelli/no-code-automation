package com.vr.test.orchestrator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestOrchestratorApplication implements CommandLineRunner {
    private final KafkaTestService kafkaTestService;

    public TestOrchestratorApplication(KafkaTestService kafkaTestService) {
        this.kafkaTestService = kafkaTestService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TestOrchestratorApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        // Send test messages
        kafkaTestService.sendMessage("rupesh", "Hello Kafka!");
        kafkaTestService.sendMessage("rupesh", "Testing Kafka cluster!");
    }
}
