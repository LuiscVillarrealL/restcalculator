package com.lcvl.challenge.rest.config;

import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class MdcFilterConfig.
 */
@Slf4j
@Component
public class MdcFilterConfig implements Filter {

  private final String correlationId = "Request-ID";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpServletRequest = (HttpServletRequest) request;

    // Retrieve or generate the requestId
    String requestId = httpServletRequest.getHeader(correlationId);
    if (requestId == null || requestId.isEmpty()) {
      requestId = UUID.randomUUID().toString();
    }

    MDC.put(correlationId, requestId);
    log.info("Intercept coming request and set MDC context information");
    // pass the request
    chain.doFilter(request, response);
  }

}
