package com.lcvl.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.lcvl.challenge.common.dto.CalculationRequest;
import com.lcvl.challenge.common.dto.CalculationResponse;
import com.lcvl.challenge.common.util.OperationEnum;
import com.lcvl.challenge.rest.controller.CalculatorRestController;
import com.lcvl.challenge.rest.exceptions.DividingByZeroException;
import com.lcvl.challenge.rest.messaging.RestKafkaMessageConsumer;
import com.lcvl.challenge.rest.messaging.RestKafkaMessageProducer;

@SpringBootTest(
    classes = { RestApplication.class },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@EnableConfigurationProperties
class CalculatorRestControllerIntegrationTest {

  @Autowired
  private CalculatorRestController calculatorRestController;

  @MockitoBean
  private RestKafkaMessageProducer kafkaMessageProducer;

  @MockitoBean
  private RestKafkaMessageConsumer kafkaMessageConsumer;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(calculatorRestController).build();
  }

  @Test
  void testGetSum() throws Exception {
    // Arrange
    String requestId = "test-id";
    CalculationResponse mockResponse = new CalculationResponse();
    mockResponse.setRequestId(requestId);
    mockResponse.setResult(BigDecimal.valueOf(15));

    // Mocking Kafka Consumer behavior
    when(kafkaMessageConsumer.getResponseById(eq(requestId), anyLong())).thenReturn(mockResponse);

    // Act & Assert
    mockMvc
        .perform(get("/api/calculator/sum").param("a", "10").param("b", "5")
            .header("Request-ID", requestId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.result").value("15"))
        .andExpect(header().string("Request-ID", requestId));

    // Verify that KafkaProducer was called with the correct request
    ArgumentCaptor<CalculationRequest> captor = ArgumentCaptor.forClass(CalculationRequest.class);
    verify(kafkaMessageProducer, times(1)).sendMessage(captor.capture(), eq(requestId));

    CalculationRequest capturedRequest = captor.getValue();
    assert capturedRequest != null;
    assert capturedRequest.getNum1().equals(BigDecimal.TEN);
    assert capturedRequest.getNum2().equals(BigDecimal.valueOf(5));
    assert capturedRequest.getOperation() == OperationEnum.SUM;
    assert capturedRequest.getRequestId().equals(requestId);
  }

  @Test
  void testGetSubtraction() throws Exception {
    // Arrange
    String requestId = "test-sub-id";
    CalculationResponse mockResponse = new CalculationResponse();
    mockResponse.setRequestId(requestId);
    mockResponse.setResult(BigDecimal.valueOf(5));

    // Mocking Kafka Consumer behavior
    when(kafkaMessageConsumer.getResponseById(eq(requestId), anyLong())).thenReturn(mockResponse);

    // Act & Assert
    mockMvc
        .perform(get("/api/calculator/sub").param("a", "10").param("b", "5")
            .header("Request-ID", requestId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.result").value("5"))
        .andExpect(header().string("Request-ID", requestId));

    // Verify KafkaProducer
    ArgumentCaptor<CalculationRequest> captor = ArgumentCaptor.forClass(CalculationRequest.class);
    verify(kafkaMessageProducer, times(1)).sendMessage(captor.capture(), eq(requestId));

    CalculationRequest capturedRequest = captor.getValue();
    assert capturedRequest.getNum1().equals(BigDecimal.TEN);
    assert capturedRequest.getNum2().equals(BigDecimal.valueOf(5));
    assert capturedRequest.getOperation() == OperationEnum.SUBTRACT;
    assert capturedRequest.getRequestId().equals(requestId);
  }

  @Test
  void testGetMultiplication() throws Exception {
    // Arrange
    String requestId = "test-multi-id";
    CalculationResponse mockResponse = new CalculationResponse();
    mockResponse.setRequestId(requestId);
    mockResponse.setResult(BigDecimal.valueOf(50));

    // Mocking Kafka Consumer behavior
    when(kafkaMessageConsumer.getResponseById(eq(requestId), anyLong())).thenReturn(mockResponse);

    // Act & Assert
    mockMvc
        .perform(get("/api/calculator/multi").param("a", "10").param("b", "5")
            .header("Request-ID", requestId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.result").value("50"))
        .andExpect(header().string("Request-ID", requestId));

    // Verify KafkaProducer
    ArgumentCaptor<CalculationRequest> captor = ArgumentCaptor.forClass(CalculationRequest.class);
    verify(kafkaMessageProducer, times(1)).sendMessage(captor.capture(), eq(requestId));

    CalculationRequest capturedRequest = captor.getValue();
    assert capturedRequest.getNum1().equals(BigDecimal.TEN);
    assert capturedRequest.getNum2().equals(BigDecimal.valueOf(5));
    assert capturedRequest.getOperation() == OperationEnum.MULTIPLY;
    assert capturedRequest.getRequestId().equals(requestId);
  }

  @Test
  void testGetDivision() throws Exception {
    // Arrange
    String requestId = "test-div-id";
    CalculationResponse mockResponse = new CalculationResponse();
    mockResponse.setRequestId(requestId);
    mockResponse.setResult(BigDecimal.valueOf(2));

    // Mocking Kafka Consumer behavior
    when(kafkaMessageConsumer.getResponseById(eq(requestId), anyLong())).thenReturn(mockResponse);

    // Act & Assert
    mockMvc
        .perform(get("/api/calculator/div").param("a", "10").param("b", "5")
            .header("Request-ID", requestId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.result").value("2"))
        .andExpect(header().string("Request-ID", requestId));

    // Verify KafkaProducer
    ArgumentCaptor<CalculationRequest> captor = ArgumentCaptor.forClass(CalculationRequest.class);
    verify(kafkaMessageProducer, times(1)).sendMessage(captor.capture(), eq(requestId));

    CalculationRequest capturedRequest = captor.getValue();
    assert capturedRequest.getNum1().equals(BigDecimal.TEN);
    assert capturedRequest.getNum2().equals(BigDecimal.valueOf(5));
    assert capturedRequest.getOperation() == OperationEnum.DIVIDE;
    assert capturedRequest.getRequestId().equals(requestId);
  }

  @Test
  void testGetDivisionByZeroDirect() {
    String requestId = "test-div-zero-id";

    DividingByZeroException exception = assertThrows(DividingByZeroException.class, () -> {
      calculatorRestController.getDivision(BigDecimal.TEN, BigDecimal.ZERO, requestId);
    });

    assertEquals("Division by zero is not allowed", exception.getMessage());
  }

  @Test
  void testGetSumTimeout() throws Exception {
    // Arrange
    String requestId = "test-timeout-id";

    // Mocking Kafka Consumer behavior to simulate timeout
    when(kafkaMessageConsumer.getResponseById(eq(requestId), anyLong())).thenReturn(null);

    // Act & Assert
    mockMvc
        .perform(get("/api/calculator/sum").param("a", "10").param("b", "5")
            .header("Request-ID", requestId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isRequestTimeout())
        .andExpect(jsonPath("$.result").value("Timeout waiting for response"));
  }
}
