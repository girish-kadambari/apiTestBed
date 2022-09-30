package com.testsigma.api_bed_test.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class User implements UserDetails, OidcUser {

  private String email;
  private String password;
  private String picture;
  private Map<String, Object> attributes;
  private UserRole role;
  private String sessionId;


  @Override
  public String getName() {
    return this.email;
  }

  @Override
  public Map<String, Object> getAttributes() {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("email", email);
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getUsername() {
    return getName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }


  public Map<String, Object> claims;
  public OidcUserInfo userInfo;
  public OidcIdToken IdToken;
}
