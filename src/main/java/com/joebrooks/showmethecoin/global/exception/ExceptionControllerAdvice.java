package com.joebrooks.showmethecoin.global.exception;

import com.joebrooks.showmethecoin.global.exception.type.AutomationException;
import com.joebrooks.showmethecoin.global.exception.type.LoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler(LoginException.class)
    public String loginException(LoginException e){
        log.warn("\n잘못된 접속 시도 " +
                "\nid: {}" +
                "\npw: {}", e.getId(), e.getPw());
        return "login";
    }

    @ExceptionHandler(IllegalAccessError.class)
    public String illegalAccess(){
        log.warn("\n잘못된 액세스 시도");

        return "login";
    }

    @ExceptionHandler(AutomationException.class)
    public void automationException(AutomationException e){
        log.warn("\n자동 매매 에러" +
                "\n제목: {}" +
                "\n에러: {}", e.getMessage(),
                e.getStackTrace());
    }

}
