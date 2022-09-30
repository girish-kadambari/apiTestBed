package com.testsigma.api_bed_test.security;


import com.testsigma.api_bed_test.config.ApplicationConfig;
import com.testsigma.api_bed_test.constants.URLConstants;
import com.testsigma.api_bed_test.execption.JwtTokenMissingException;
import com.testsigma.api_bed_test.service.JwtTokenGeneratorService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class BearerAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    ApplicationConfig applicationConfig;

    @Autowired
    JwtTokenGeneratorService jwtTokenGeneratorService;

    public BearerAuthenticationFilter() {
        super(URLConstants.BEARER + "/**");
    }


    private final RequestMatcher bearerRequestMatcher = new AntPathRequestMatcher(URLConstants.BEARER + "/**");

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new JwtTokenMissingException("No JWT token found in request headers");
        }
        try {
            String authToken = header.substring(7);
            Claims body = parseJwtToken(authToken);
            String username = body.get("username").toString();
            String password =  body.get("password").toString();
            if (!username.equals(applicationConfig.getUsername()) && password.equals(applicationConfig.getPassword())) {
                throw new BadCredentialsException("Unauthorised Access for user : " + username);
            }
            return new UsernamePasswordAuthenticationToken(null,null, null);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            throw new BadCredentialsException(exception.getMessage());
        }
    }

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
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(failed.getMessage());
    }

    public Claims parseJwtToken(String token){
        return Jwts.parser()
                .setSigningKey(applicationConfig.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();
    }

}
