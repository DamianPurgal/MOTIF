package com.deemor.motif.user.exception;

import com.deemor.motif.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserUsernameNotAvaliableException extends BusinessException {

    public UserUsernameNotAvaliableException() { super(HttpStatus.BAD_REQUEST, "Username arleady exists!"); }

}
