package com.testsigma.api_bed_test.service;


import com.testsigma.api_bed_test.security.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class OidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Override
    public OidcUser loadUser(OidcUserRequest oidcUserRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = new org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService().loadUser(oidcUserRequest);

        String email = oidcUser.getAttributes().get("email").toString();
        if (!email.endsWith("@testsigma.com")) {
            throw new InternalAuthenticationServiceException("This space is for testsigma employees");
        }
        try {
            User user = new User();
            user.setEmail(email);
            user.setClaims(oidcUser.getClaims());
            user.setPicture(oidcUser.getPicture());
            user.setUserInfo(oidcUser.getUserInfo());
            user.setIdToken(oidcUser.getIdToken());
            return user;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
}
