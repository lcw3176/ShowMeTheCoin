package com.joebrooks.showmethecoin.auth;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

@Slf4j
public class AuthFilter implements Filter {
    private static final String[] whitelist = {"/login", "/", "/home", "/favicon.ico", "/css/*", "/js/*", "/img/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            if (isLoginCheckPath(requestURI)) {
                HttpSession session = httpRequest.getSession(false);

                if (session == null || session.getAttribute("userId") == null) {

                    httpResponse.sendRedirect("/login");
                    return;
                }
            }

            ((HttpServletResponse) response).addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            ((HttpServletResponse) response).addHeader("Pragma", "no-cache");
            ((HttpServletResponse) response).addHeader("Expires", "0");

            chain.doFilter(request, response);

        } catch (Exception e) {
            throw new AuthException(e.getMessage(), e);
        }
    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }


}

