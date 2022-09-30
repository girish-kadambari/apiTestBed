package com.testsigma.api_bed_test.execption;

import org.springframework.security.core.AuthenticationException;

public class BasicAuthorizationHeaderMissingException  extends AuthenticationException {
    public BasicAuthorizationHeaderMissingException(String msg) {
        super(msg);
    }
}
