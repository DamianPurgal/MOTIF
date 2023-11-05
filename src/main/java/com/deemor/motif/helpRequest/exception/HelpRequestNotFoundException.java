package com.deemor.motif.helpRequest.exception;

import com.deemor.motif.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class HelpRequestNotFoundException extends BusinessException {

    public HelpRequestNotFoundException() { super(HttpStatus.NOT_FOUND, "Help request not found"); }

}
