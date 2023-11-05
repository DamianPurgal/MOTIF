package com.deemor.motif.security.exception.authentication;

import org.springframework.http.HttpStatus;
import com.deemor.motif.exception.BusinessException;

public class AuthenticationFailureException extends BusinessException {

    public AuthenticationFailureException() { super(HttpStatus.UNAUTHORIZED, "Authentication failure"); }

}
