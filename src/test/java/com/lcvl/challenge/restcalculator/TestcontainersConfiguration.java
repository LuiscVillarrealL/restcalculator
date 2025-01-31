package com.lcvl.challenge.restcalculator;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * The Class TestcontainersConfiguration.
 */
@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

  /**
   * Kafka container.
   *
   * @return the kafka container
   */
  @Bean
  @ServiceConnection
  KafkaContainer kafkaContainer() {
    return new KafkaContainer(DockerImageName.parse("apache/kafka-native:latest"));
  }

}
