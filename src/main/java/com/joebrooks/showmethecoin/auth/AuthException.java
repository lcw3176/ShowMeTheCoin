package com.joebrooks.showmethecoin.auth;

public class AuthException extends RuntimeException {

    public AuthException(String msg){
        super(msg);
    }

    public AuthException(String msg, Throwable e){
        super(msg, e);
    }
}
