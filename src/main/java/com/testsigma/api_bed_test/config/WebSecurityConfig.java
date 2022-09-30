package com.testsigma.api_bed_test.config;


import com.testsigma.api_bed_test.constants.URLConstants;
import com.testsigma.api_bed_test.security.*;
import com.testsigma.api_bed_test.service.OidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.testsigma.api_bed_test.security.AjaxLoginFormConfigurer.ajaxLogin;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final WebApplicationContext webApplicationContext;
    private final OidcUserService userService;
    private final ApplicationConfig applicationConfig;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AjaxLoginFailureHandler ajaxLoginFailureHandler() {
        return new AjaxLoginFailureHandler();
    }

    @Bean
    public AjaxLoginSuccessHandler ajaxLoginSuccessHandler() {
        return new AjaxLoginSuccessHandler();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieAuthorizationRequestRepository() {
        return new com.testsigma.api_bed_test.security.HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SessionAuthenticationFilter sessionAuthenticationFilter() throws Exception {
        SessionAuthenticationFilter filter = new SessionAuthenticationFilter("/**/*");
        filter.setAuthenticationManager(super.authenticationManagerBean());
        filter.setAuthenticationFailureHandler(ajaxLoginFailureHandler());
        return filter;
    }


    @Bean
    public BearerAuthenticationFilter bearerAuthenticationFilter() throws Exception {
        BearerAuthenticationFilter filter = new BearerAuthenticationFilter();
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }
    @Bean
    public CustomBasicAuthFilter customBasicAuthFilter() throws Exception {
        CustomBasicAuthFilter filter = new CustomBasicAuthFilter();
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }
    @Bean
    public APIKeyAuthenticationFilter apiKeyAuthenticationFilter() throws Exception {
        APIKeyAuthenticationFilter filter = new APIKeyAuthenticationFilter();
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://dev.testsigma.com", "http://localhost:8080",
                "http://dev-jarvis.testsigma.com", "https://*.testsigma.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("content-type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/assets/**","/login", "/oauth/authorize")
                .antMatchers("/error")
                .antMatchers(URLConstants.NO_AUTH+"/**");
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable()
                .addHeaderWriter(
                        new StaticHeadersWriter("X-Content-Security-Policy", "frame-ancestors http://dev.testsigma.com " +
                                "http://localhost:8084 http://localhost:8080 https://*.testsigma.com http://dev-id.testsigma.com"))
                .addHeaderWriter(
                        new StaticHeadersWriter("Content-Security-Policy", "frame-ancestors http://dev.testsigma.com " +
                                "http://localhost:8084 http://localhost:8080 https://*.testsigma.com http://dev-id.testsigma.com"))
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors()
                .and().csrf().disable()
                .authorizeRequests().antMatchers("/assets/**").permitAll()
                .antMatchers(URLConstants.NO_AUTH).permitAll()
                .antMatchers("/**/*").authenticated().and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint()).and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(URLConstants.LOGOUT_URL, "GET"))
                .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))).deleteCookies("JSESSIONID", applicationConfig.getCookieName())
                .invalidateHttpSession(true).and()
                .apply(ajaxLogin(super.authenticationManagerBean(), webApplicationContext))
                .loginPage("/login")
                .successHandler(ajaxLoginSuccessHandler()).failureHandler(ajaxLoginFailureHandler()).and()
                .addFilterBefore(webApplicationContext.getBean(SessionAuthenticationFilter.class), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(webApplicationContext.getBean(BearerAuthenticationFilter.class), SessionAuthenticationFilter.class)
                .addFilterBefore(webApplicationContext.getBean(CustomBasicAuthFilter.class),SessionAuthenticationFilter.class)
                .addFilterBefore(webApplicationContext.getBean(APIKeyAuthenticationFilter.class),SessionAuthenticationFilter.class)
                .exceptionHandling().and();
    }
}
