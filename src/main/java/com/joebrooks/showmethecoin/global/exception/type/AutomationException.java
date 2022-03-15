package com.joebrooks.showmethecoin.global.exception.type;

import lombok.Getter;

@Getter
public class AutomationException extends Exception{

    private String message;

    public AutomationException(Exception e, String message){
        super(e);
        this.message = message;
    }
}
