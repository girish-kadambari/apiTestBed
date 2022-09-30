package com.testsigma.api_bed_test.execption;
import org.springframework.security.core.AuthenticationException;

public class JwtTokenMissingException extends AuthenticationException {
    public JwtTokenMissingException(String msg) {
        super(msg);
    }
}
