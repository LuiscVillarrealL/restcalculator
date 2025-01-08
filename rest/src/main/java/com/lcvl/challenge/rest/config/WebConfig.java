package com.lcvl.challenge.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The Class WebConfig.
 */
@Configuration

public class WebConfig {

  /**
   * Cors config.
   *
   * @return the web mvc configurer
   */
  @Bean
  public WebMvcConfigurer corsConfig() {

    return new WebMvcConfigurer() {

      @Override
      public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**").allowedOrigins("http://localhost:4200")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
            .allowCredentials(true);
      }
    };

  }

}
