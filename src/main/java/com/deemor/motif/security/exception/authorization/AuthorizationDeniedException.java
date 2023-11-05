package com.deemor.motif.security.exception.authorization;

import org.springframework.http.HttpStatus;
import com.deemor.motif.exception.BusinessException;

public class AuthorizationDeniedException extends BusinessException {

    public AuthorizationDeniedException() { super(HttpStatus.FORBIDDEN, "Authorization denied"); }

}
