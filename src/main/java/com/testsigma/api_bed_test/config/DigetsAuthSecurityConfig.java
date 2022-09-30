package com.testsigma.api_bed_test.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DigetsAuthSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private DigestAuthenticationEntryPoint getDigestEntryPoint() {
        DigestAuthenticationEntryPoint digestEntryPoint = new DigestAuthenticationEntryPoint();
        digestEntryPoint.setRealmName("admin-digest-realm");
        digestEntryPoint.setKey("somedigestkey");
        return digestEntryPoint;
    }


    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    private DigestAuthenticationFilter getDigestAuthFilter() throws Exception {
        DigestAuthenticationFilter digestFilter = new DigestAuthenticationFilter();

        digestFilter.setUserDetailsService(userDetailsServiceBean());


        digestFilter.setAuthenticationEntryPoint(getDigestEntryPoint());
        return digestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/digest/**").
                addFilter(getDigestAuthFilter()).exceptionHandling()
                .authenticationEntryPoint(getDigestEntryPoint())
                .and().authorizeRequests().antMatchers("/digest/**").hasRole("ADMIN");
    }

}
