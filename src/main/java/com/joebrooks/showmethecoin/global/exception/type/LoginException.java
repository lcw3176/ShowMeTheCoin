package com.joebrooks.showmethecoin.global.exception.type;

import lombok.Getter;

@Getter
public class LoginException extends RuntimeException {

    private String id;
    private String pw;

    public LoginException(String id, String pw){
        this.id = id;
        this.pw = pw;
    }
}
