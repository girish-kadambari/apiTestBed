package com.testsigma.api_bed_test.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApplicationConfig {

    @Value("${jwt_secret}")
    private String jwtSecret;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${session.cookie_name}")
    String cookieName;

    @Value("${session.is_secure}")
    Boolean isCookieSecure;

    @Value("${session.is_http_only}")
    Boolean isHttpOnlyCookie;

    @Value("${api_auth.key}")
    String APIAuthKey;

    @Value("${api_auth.value}")
    String APIAuthValue;


}
