package com.deemor.motif.security.response.authorization;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AuthorizationErrorResponse {

    private String message;
    private HttpStatus status;

    public AuthorizationErrorResponse() {
        this.message = "Authorization denied";
        this.status = HttpStatus.FORBIDDEN;
    }
}
