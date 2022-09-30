package com.testsigma.api_bed_test.security;

import com.testsigma.api_bed_test.service.ObjectMapperService;
import com.testsigma.api_bed_test.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@Log4j2
public class AjaxUserNamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  WebApplicationContext webApplicationContext;
  UserService userService;

  public AjaxUserNamePasswordAuthenticationFilter(WebApplicationContext webApplicationContext) {
    super(new AntPathRequestMatcher("/login", "POST"));
    this.webApplicationContext = webApplicationContext;
    userService = webApplicationContext.getBean(UserService.class);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException {
    LoginRequest loginData = getPostData(request);
    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
        loginData.getUsername(), loginData.getPassword());
    authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    Authentication auth = this.getAuthenticationManager().authenticate(authRequest);
    return auth;
  }

  private LoginRequest getPostData(HttpServletRequest request) throws BadCredentialsException, IOException {
    LoginRequest loginRequest = new LoginRequest();
    if (request.getParameter("username") == null && request.getParameter("password") == null) {
      BufferedReader reader = request.getReader();
      StringBuilder sb = new StringBuilder();
      String line = reader.readLine();
      while (line != null) {
        sb.append(line).append("\n");
        line = reader.readLine();
      }
      reader.close();
      loginRequest = new ObjectMapperService().parseJson(sb.toString(), LoginRequest.class);
    } else {
      loginRequest.setUsername(request.getParameter("username"));
      loginRequest.setPassword(request.getParameter("password"));
    }
    return loginRequest;
  }
}
