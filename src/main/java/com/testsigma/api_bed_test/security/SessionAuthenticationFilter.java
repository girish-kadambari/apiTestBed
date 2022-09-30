package com.testsigma.api_bed_test.security;

import com.testsigma.api_bed_test.config.ApplicationConfig;
import com.testsigma.api_bed_test.model.SpringSession;
import com.testsigma.api_bed_test.service.JwtTokenGeneratorService;
import com.testsigma.api_bed_test.service.SpringSessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Log4j2
public class SessionAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final RequestMatcher loginRequestMatcher = new AntPathRequestMatcher(URLConstants.LOGIN_URL + "/**");
    private final RequestMatcher assetsRequestMatcher = new AntPathRequestMatcher(URLConstants.ASSETS_URL + "/**");
    private final RequestMatcher sessionRequestMatcher = new AntPathRequestMatcher(URLConstants.SESSIONS_URL);
    private final RequestMatcher noAuthRequestMatcher = new AntPathRequestMatcher(com.testsigma.api_bed_test.constants.URLConstants.NO_AUTH + "/**");

    private final RequestMatcher bearerAuthRequestMatcher = new AntPathRequestMatcher(com.testsigma.api_bed_test.constants.URLConstants.BEARER + "/**");

    private final RequestMatcher basicAuthRequestMatcher = new AntPathRequestMatcher(com.testsigma.api_bed_test.constants.URLConstants.BASIC_AUTH + "/**");
    private final RequestMatcher apiKeyAuthRequestMatcher = new AntPathRequestMatcher(com.testsigma.api_bed_test.constants.URLConstants.API_KEY_AUTH + "/**");

    private final RequestMatcher oauth2AuthRequestMatcher = new AntPathRequestMatcher(  "oauth2/**");



    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    SpringSessionService springSessionService;
    @Autowired
    JwtTokenGeneratorService jwtTokenGeneratorService;
    @Autowired
    private ApplicationConfig applicationConfig;

    public SessionAuthenticationFilter(String string) {
        super(string);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        //log.info("Attempting Authentication from session - " + request.getSession().getId());

        Authentication auth = null;
        User user = null;
        String cookieValue = getJWTCookieValue(request);
        if (cookieValue != null) {
            user = jwtTokenGeneratorService.parseSessionId(cookieValue);
            Optional<SpringSession> springSession = springSessionService.findById(user.getSessionId());
            if (springSession.isPresent()) {
                user.setRole(UserRole.USER);
                //log.info("Retrieved User From Session --> " + user);
                auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            } else {
                // log.info("No User Found with uuid");
            }
        } else {
            String sessionId = request.getSession().getId();
            Object email = request.getSession().getAttribute("email");
            Object picture = request.getSession().getAttribute("picture");
            if (email != null) {
                Optional<SpringSession> springSession = springSessionService.findBySessionId(sessionId);
                user = new User();
                if (springSession.isPresent())
                    user.setSessionId(springSession.get().getPrimaryId());
                user.setEmail(email.toString());
                user.setPicture(picture.toString());
                user.setRole(UserRole.USER);
                auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                if (springSession.isPresent() && !isSessionRequest(request)) {
                    String token = jwtTokenGeneratorService.generateSessionToken(user);
                    Cookie cookie = new Cookie(applicationConfig.getCookieName(), token);
                    cookie.setSecure(applicationConfig.getIsCookieSecure());
                    cookie.setHttpOnly(applicationConfig.getIsHttpOnlyCookie());
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }

        if (isSessionRequest(request) && auth == null) {
            auth = new UsernamePasswordAuthenticationToken(null, null, null);
        }
        if (auth == null) {
            throw new BadCredentialsException("AUTH TOKEN MISSING");
        }
        return auth;
    }


    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return super.requiresAuthentication(request, response) && !isLoginRequest(request)
                && !isLoginRequest(request) && !isAssetsRequest(request)
                && !isNoAuthRequest(request) && !isBearerAuth(request)
                && !isBasicAuthRequest(request) &&!isAPIKeyAuthRequest(request) && !oauth2AuthRequestMatcher.matches(request);

    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return loginRequestMatcher.matches(request);
    }

    private boolean isAssetsRequest(HttpServletRequest request) {
        return assetsRequestMatcher.matches(request);
    }

    private boolean isNoAuthRequest(HttpServletRequest request) {
        return noAuthRequestMatcher.matches(request);
    }


    private boolean isSessionRequest(HttpServletRequest request) {
        return sessionRequestMatcher.matches(request);
    }

    private boolean isBearerAuth(HttpServletRequest request) {
        return bearerAuthRequestMatcher.matches(request);
    }

    private boolean isBasicAuthRequest(HttpServletRequest request) {
        return basicAuthRequestMatcher.matches(request);
    }

    private boolean isAPIKeyAuthRequest(HttpServletRequest request) {
        return apiKeyAuthRequestMatcher.matches(request);
    }
    private boolean isOauth2AuthRequest(HttpServletRequest request) {
        return oauth2AuthRequestMatcher.matches(request);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        if (authResult != null) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);
        }
        chain.doFilter(request, response);
    }


    private String getJWTCookieValue(HttpServletRequest request) {
        String cookieValue = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(applicationConfig.getCookieName())) {
                    cookieValue = cookie.getValue();
                }
            }
        }
        return cookieValue;
    }

}
