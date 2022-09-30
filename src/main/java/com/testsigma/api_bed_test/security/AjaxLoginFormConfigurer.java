package com.testsigma.api_bed_test.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.WebApplicationContext;

public class AjaxLoginFormConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractAuthenticationFilterConfigurer<H,
                AjaxLoginFormConfigurer<H>,
                AjaxUserNamePasswordAuthenticationFilter> {

  public static AjaxLoginFormConfigurer<HttpSecurity> ajaxLogin(AuthenticationManager authenticationManager,
                                                                WebApplicationContext webApplicationContext) {
    return new AjaxLoginFormConfigurer<>(authenticationManager, webApplicationContext);
  }

  public AjaxLoginFormConfigurer(AuthenticationManager authenticationManager, WebApplicationContext webApplicationContext) {
    super(new AjaxUserNamePasswordAuthenticationFilter(webApplicationContext), null);
    this.getAuthenticationFilter().setAuthenticationManager(authenticationManager);
    this.getAuthenticationFilter().setAuthenticationSuccessHandler(webApplicationContext.getBean(AjaxLoginSuccessHandler.class));
    this.getAuthenticationFilter().setAuthenticationFailureHandler(webApplicationContext.getBean(AjaxLoginFailureHandler.class));
  }

  @Override
  public AjaxLoginFormConfigurer<H> loginPage(String loginPage) {
    return super.loginPage("/login");
  }

  @Override
  public void init(H http) throws Exception {
    HttpSecurity httpSecurity = ((HttpSecurity) http);
    httpSecurity.addFilterAt(this.getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    initDefaultLoginFilter(http);
  }

  @Override
  public void configure(H http) throws Exception {
    super.configure(http);
  }

  @Override
  protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
    return new AntPathRequestMatcher(loginProcessingUrl, "POST");
  }

  private void initDefaultLoginFilter(H http) {

  }
}