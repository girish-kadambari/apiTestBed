package com.testsigma.api_bed_test.security;

import com.testsigma.api_bed_test.config.ApplicationConfig;
import com.testsigma.api_bed_test.constants.URLConstants;
import com.testsigma.api_bed_test.execption.BasicAuthorizationHeaderMissingException;
import com.testsigma.api_bed_test.execption.JwtTokenMissingException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

public class CustomBasicAuthFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    ApplicationConfig applicationConfig;


    public CustomBasicAuthFilter() {
        super(URLConstants.BASIC_AUTH + "/**");
    }



    private final RequestMatcher basicAuthRequestMatcher = new AntPathRequestMatcher(URLConstants.BASIC_AUTH + "/**");


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (authResult != null) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return super.requiresAuthentication(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Basic ")) {
            throw new BasicAuthorizationHeaderMissingException("Authorization header not found.");
        }
        try {
            String credentials = decodeBase64(header.substring(6));
            String[] credentialsArray = credentials.split(":");
            if(credentialsArray.length < 2){
                throw new BadCredentialsException("Bad Credentials");
            }

            if (!credentialsArray[0].equals(applicationConfig.getUsername()) && !credentialsArray[1].equals(applicationConfig.getPassword())) {
                throw new BadCredentialsException("Unauthorised User");
            }
            return new UsernamePasswordAuthenticationToken(null,null, null);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new BadCredentialsException(exception.getMessage());
        }
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(failed.getMessage());
    }

    public String decodeBase64(String encodedString){
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);
        return decodedString;
    }

}
