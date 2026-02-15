package com.vr.test.orchestrator;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic testTopic() {
        return new NewTopic("rupesh", 3, (short) 3); // partitions, replication factor
    }
}
