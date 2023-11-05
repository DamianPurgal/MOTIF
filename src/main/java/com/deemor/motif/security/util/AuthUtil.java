package com.deemor.motif.security.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    public static String getLoggedUserUsername(){
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
