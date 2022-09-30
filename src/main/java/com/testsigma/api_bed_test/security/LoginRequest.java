package com.testsigma.api_bed_test.security;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
  @NotBlank @Email private String username;

  @NotBlank private String password;

  private Boolean product_login = Boolean.FALSE;
}
