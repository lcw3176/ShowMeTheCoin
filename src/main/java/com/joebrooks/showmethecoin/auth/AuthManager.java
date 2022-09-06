package com.joebrooks.showmethecoin.auth;

import com.joebrooks.showmethecoin.user.UserEntity;
import com.joebrooks.showmethecoin.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class AuthManager {

    public static final String SESSION_KEY = "userId";
    private final UserService userService;

    public UserEntity extractUserId(HttpSession session){
        Object key = session.getAttribute(SESSION_KEY);

        if(key == null){
            throw new AuthException("존재하지 않는 유저 세션");
        }

        return userService.getUser(key.toString());
    }

    public void saveSession(HttpSession session, String userId){
        session.setAttribute(SESSION_KEY, userId);
    }


    public void removeSession(HttpSession session){
        session.removeAttribute(SESSION_KEY);
    }
}
