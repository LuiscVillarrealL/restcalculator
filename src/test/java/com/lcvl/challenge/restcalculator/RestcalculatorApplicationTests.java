package com.lcvl.challenge.restcalculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RestcalculatorApplicationTests {

	@Test
	void contextLoads() {
	}

}