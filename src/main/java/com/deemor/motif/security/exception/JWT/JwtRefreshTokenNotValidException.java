package com.deemor.motif.security.exception.JWT;

import org.springframework.http.HttpStatus;
import com.deemor.motif.exception.BusinessException;

public class JwtRefreshTokenNotValidException extends BusinessException {

    public JwtRefreshTokenNotValidException() { super(HttpStatus.BAD_REQUEST, "Refresh token not valid"); }

}
