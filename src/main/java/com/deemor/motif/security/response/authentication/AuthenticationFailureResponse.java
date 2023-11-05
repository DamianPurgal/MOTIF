package com.deemor.motif.security.response.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AuthenticationFailureResponse {

    private String message;
    private HttpStatus status;

    public AuthenticationFailureResponse() {
        this.message = "Authentication failure";
        this.status = HttpStatus.UNAUTHORIZED;
    }
}
