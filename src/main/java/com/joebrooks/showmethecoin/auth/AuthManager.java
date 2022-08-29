package com.joebrooks.showmethecoin.auth;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpSession;

@UtilityClass
public class AuthManager {

    private static final String SESSION_KEY = "userId";


    public String extractUserId(HttpSession session){
        Object key = session.getAttribute(SESSION_KEY);

        if(key == null){
            throw new AuthException("존재하지 않는 유저 세션");
        }

        return key.toString();
    }

    public boolean isExist(HttpSession session){
        return session.getAttribute(SESSION_KEY) != null;
    }

    public void saveSession(HttpSession session, String userId){
        session.setAttribute(SESSION_KEY, userId);
    }


    public void removeSession(HttpSession session){
        session.removeAttribute(SESSION_KEY);
    }
}
