package com.lcvl.challenge.restcalculator;

import org.springframework.boot.SpringApplication;

public class TestRestcalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.from(RestcalculatorApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
