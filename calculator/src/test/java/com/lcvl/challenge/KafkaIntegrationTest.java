package com.lcvl.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import com.lcvl.challenge.calculator.CalculatorApplication;
import com.lcvl.challenge.calculator.dto.CalculationRequest;
import com.lcvl.challenge.calculator.dto.CalculationResponse;
import com.lcvl.challenge.calculator.util.OperationEnum;

/**
 * The Class KafkaIntegrationTest.
 */
@SpringBootTest(
    classes = CalculatorApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class KafkaIntegrationTest {

  @Container
  static final KafkaContainer kafka = new KafkaContainer(
      DockerImageName.parse("apache/kafka-native:3.8.0"));

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    // Ensure the container has started before accessing its properties
    kafka.start();
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @Autowired
  private KafkaTemplate<String, CalculationRequest> kafkaTemplate;

  @Value("${kafka.request-topic}")
  private String requestTopic;

  @Value("${kafka.result-topic}")
  private String resultTopic;

  private final BlockingQueue<CalculationResponse> responseQueue = new LinkedBlockingQueue<>();

  /**
   * Consume result.
   *
   * @param record the record
   */
  @KafkaListener(
      topics = "${kafka.result-topic}",
      groupId = "${spring.kafka.consumer.group-id}",
      concurrency = "2")
  public void consumeResult(ConsumerRecord<String, CalculationResponse> record) {
    responseQueue.offer(record.value());
  }

  @Test
  void testCalculationFlow() throws InterruptedException {
    CalculationRequest request = new CalculationRequest("123", OperationEnum.SUM, BigDecimal.TEN,
        BigDecimal.ONE);
    kafkaTemplate.send(new ProducerRecord<>(requestTopic, "testKey", request));

    CalculationResponse response = responseQueue.poll(5, TimeUnit.SECONDS);
    assertThat(response).isNotNull();
    assertThat(response.getRequestId()).isEqualTo("123");
    assertThat(response.getResult()).isEqualTo(new BigDecimal("11"));
    assertThat(response.getError()).isNull();
  }

  @Test
  void testCalculationFlowWithError() throws InterruptedException {
    CalculationRequest request = new CalculationRequest("124", OperationEnum.DIVIDE, BigDecimal.TEN,
        BigDecimal.ZERO);
    kafkaTemplate.send(new ProducerRecord<>(requestTopic, "testKey", request));

    CalculationResponse response = responseQueue.poll(5, TimeUnit.SECONDS);
    assertThat(response).isNotNull();
    assertThat(response.getRequestId()).isEqualTo("124");
    assertThat(response.getResult()).isNull();
    assertThat(response.getError()).isEqualTo("Division by zero is not allowed.");
  }
}
