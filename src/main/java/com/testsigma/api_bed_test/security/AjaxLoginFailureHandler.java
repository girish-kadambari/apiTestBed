package com.testsigma.api_bed_test.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testsigma.api_bed_test.dto.APIErrorDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class AjaxLoginFailureHandler implements AuthenticationFailureHandler {

  @Autowired
  ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException {
    //log.info("Failed authentication...redirecting to login page");
    if (exception instanceof InternalAuthenticationServiceException) {
      response.sendRedirect("/ui/login?error=no_email");
    }
    APIErrorDTO errorResponse = new APIErrorDTO();
    errorResponse.setError(exception.getLocalizedMessage());
    response.setContentType("application/json;charset=UTF-8");
    response.setHeader("Cache-Control", "no-cache");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}