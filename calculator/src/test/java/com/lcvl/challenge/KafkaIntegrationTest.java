package com.lcvl.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import com.lcvl.challenge.calculator.CalculatorApplication;
import com.lcvl.challenge.calculator.dto.CalculationRequest;
import com.lcvl.challenge.calculator.dto.CalculationResponse;
import com.lcvl.challenge.calculator.util.OperationEnum;

@EmbeddedKafka(
    partitions = 1,
    topics = { "test-calculation-request-topic", "test-calculation-result-topic" })
@SpringBootTest(
    classes = CalculatorApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class KafkaIntegrationTest {

  @Autowired
  private KafkaTemplate<String, CalculationRequest> kafkaTemplate;

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

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
  @KafkaListener(topics = "${kafka.result-topic}", groupId = "${spring.kafka.consumer.group-id}")
  public void consumeResult(ConsumerRecord<String, CalculationResponse> record) {
    responseQueue.offer(record.value());
  }

  @BeforeEach
  void setUp() {
    resetKafkaTopics();
    responseQueue.clear();
  }

  private void resetKafkaTopics() {
    try (AdminClient adminClient = AdminClient.create(Map.of(
        AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString()))) {
      adminClient
          .deleteTopics(List.of("test-calculation-request-topic", "test-calculation-result-topic"))
          .all().get();

      TimeUnit.SECONDS.sleep(2);
    } catch (Exception e) {
      System.err.println("Error resetting Kafka topics: " + e.getMessage());
    }
  }

  @Test
  @Order(1)
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
  @Order(2)
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
