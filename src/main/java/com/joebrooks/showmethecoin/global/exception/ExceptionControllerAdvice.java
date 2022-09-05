package com.joebrooks.showmethecoin.global.exception;

import com.joebrooks.showmethecoin.auth.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler(AuthException.class)
    public String AuthExceptionHandler(AuthException e){
        log.error(e.getMessage());
        return "login";
    }

    @ExceptionHandler(IllegalAccessError.class)
    public String illegalAccessHandler(IllegalAccessError e){
        log.error(e.getMessage() , e);

        return "login";
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e){
        log.error(e.getMessage() , e);

        return "login";
    }


}
