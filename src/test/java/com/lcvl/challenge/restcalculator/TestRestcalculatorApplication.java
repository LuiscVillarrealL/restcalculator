package com.lcvl.challenge.restcalculator;

import org.springframework.boot.SpringApplication;

/**
 * The Class TestRestcalculatorApplication.
 */
public class TestRestcalculatorApplication {

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    SpringApplication.from(RestcalculatorApplication::main).with(TestcontainersConfiguration.class)
        .run(args);
  }

}
