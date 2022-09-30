package com.testsigma.api_bed_test.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class AjaxLoginSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;charset=UTF-8");
    response.setHeader("Cache-Control", "no-cache");

    User user = (User) authentication.getPrincipal();
    HttpSession session = request.getSession();
    session.setAttribute("email", user.getEmail());
    session.setAttribute("picture", user.getPicture());

    clearOauthCookies(request, response);
    checkRedirects(request, response, authentication);
  }

  private void checkRedirects(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    response.sendRedirect("/ui/");
  }

  public void clearOauthCookies(HttpServletRequest request, HttpServletResponse response) {
    List<String> cookiesToClear = Arrays.asList(
        HttpCookieOAuth2AuthorizationRequestRepository.AUTHORIZATION_REQUEST_COOKIE_NAME,
        HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_TO_COOKIE_PARAM_NAME
    );
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookiesToClear.contains(cookie.getName())) {
          cookie.setValue("");
          cookie.setMaxAge(0);
          cookie.setPath("/");
          response.addCookie(cookie);
        }
      }
    }
  }
}
