package com.testsigma.api_bed_test.security;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

public class HttpCookieOAuth2AuthorizationRequestRepository implements
        AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
  public static final String REDIRECT_TO_COOKIE_PARAM_NAME = "redirectTo";
  public static final String AUTHORIZATION_REQUEST_COOKIE_NAME = "jarvis_oauth2_authorization_request";

  /**
   * Utility for deleting related cookies
   */
  private static void deleteCookies(HttpServletRequest request, HttpServletResponse response) {

    Cookie[] cookies = request.getCookies();

    if (cookies != null && cookies.length > 0)
      for (Cookie cookie : cookies)
        if (cookie.getName().equals(AUTHORIZATION_REQUEST_COOKIE_NAME) ||
                cookie.getName().equals(REDIRECT_TO_COOKIE_PARAM_NAME)) {

          cookie.setValue("");
          cookie.setPath("/");
          cookie.setMaxAge(0);
          response.addCookie(cookie);
        }
  }

  private static Optional<Cookie> fetchCookie(HttpServletRequest request, String name) {

    Cookie[] cookies = request.getCookies();

    if (cookies != null && cookies.length > 0)
      for (Cookie cookie : cookies)
        if (cookie.getName().equals(name))
          return Optional.of(cookie);

    return Optional.empty();
  }

  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
    Assert.notNull(request, "request cannot be null");
    Cookie redirectTo = fetchCookie(request, REDIRECT_TO_COOKIE_PARAM_NAME).orElse(null);
    if(redirectTo != null) {
      request.setAttribute(REDIRECT_TO_COOKIE_PARAM_NAME, redirectTo.getValue());
    }
    return fetchCookie(request, AUTHORIZATION_REQUEST_COOKIE_NAME)
            .map(this::deserialize)
            .orElse(null);
  }

  @Override
  public void saveAuthorizationRequest(
          OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
          HttpServletResponse response) {
    Assert.notNull(request, "request cannot be null");
    Assert.notNull(response, "response cannot be null");

    if (authorizationRequest == null) {
      deleteCookies(request, response);
      return;
    }

    Cookie cookie = new Cookie(AUTHORIZATION_REQUEST_COOKIE_NAME, serialize(authorizationRequest));
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(600);
    response.addCookie(cookie);

    String redirectUri = request.getParameter(REDIRECT_TO_COOKIE_PARAM_NAME);
    if (StringUtils.isNotBlank(redirectUri)) {
      cookie = new Cookie(REDIRECT_TO_COOKIE_PARAM_NAME, redirectUri);
      cookie.setPath("/");
      cookie.setHttpOnly(true);
      cookie.setMaxAge(600);
      response.addCookie(cookie);
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
    return loadAuthorizationRequest(request);
  }

  private String serialize(OAuth2AuthorizationRequest authorizationRequest) {

    return Base64.getUrlEncoder().encodeToString(
            SerializationUtils.serialize(authorizationRequest));
  }

  private OAuth2AuthorizationRequest deserialize(Cookie cookie) {

    return SerializationUtils.deserialize(
            Base64.getUrlDecoder().decode(cookie.getValue()));
  }
}