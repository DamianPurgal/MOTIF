package com.deemor.motif.user.exception;

import com.deemor.motif.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserUsernameNotFoundException extends BusinessException {

    public UserUsernameNotFoundException() { super(HttpStatus.NOT_FOUND, "Username not found"); }

}
