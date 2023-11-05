package com.deemor.motif.user.exception;

import com.deemor.motif.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserPasswordsDoesntMatchException extends BusinessException {

    public UserPasswordsDoesntMatchException() { super(HttpStatus.BAD_REQUEST, "Password doesnt match!"); }

}
